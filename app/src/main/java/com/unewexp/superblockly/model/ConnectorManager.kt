package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.utils.disconnect
import com.example.myfirstapplicatioin.utils.safeConnect
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.IfBlock
import com.unewexp.superblockly.blocks.returnBlocks.StringConcatenationBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.StringAppendBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import com.unewexp.superblockly.viewBlocks.ViewInitialSize
import kotlin.math.pow
import kotlin.math.sqrt

object ConnectorManager {
    val connetionLength = 50.0


    fun tryConnectAndDisconnectDrag(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density){

        if(!parentDisconnectOrNot(sourceDragBlock, viewModel, density)) return // Рассконекчивает, если достаточно далеко

        if(sourceDragBlock.connectedParent!= null && sourceDragBlock.connectedParentConnectionView != null) return; // Если не расконектил, то не надо и коннектить


        tryConnectBlock(sourceDragBlock, viewModel, density, sourceDragBlock.outputConnectionView!!.connector.connectionType)
    }
    private fun tryConnectBlock(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density, connectionType: ConnectorType){

        val listDragBlocks = viewModel.blocks.value.filter { it !in sourceDragBlock.scope && (it != sourceDragBlock) }.toMutableList()

        val nearestBlock: DraggableBlock?
        val nearestConnection: ConnectionView?

        when(connectionType){
            ConnectorType.OUTPUT -> {
                nearestBlock = getBlockWithNearestConnection(sourceDragBlock, listDragBlocks, density, ConnectorType.INPUT, false)
                nearestConnection = getNearestConnection(sourceDragBlock, listDragBlocks, density, ConnectorType.INPUT)
            }
            ConnectorType.STRING_TOP -> {
                nearestBlock = getBlockWithNearestConnection(sourceDragBlock, listDragBlocks, density, ConnectorType.STRING_BOTTOM, true)
                nearestConnection = getNearestConnection(sourceDragBlock, listDragBlocks, density, ConnectorType.STRING_BOTTOM)

            }
            else -> throw IllegalArgumentException("Коннектор типа ${sourceDragBlock.outputConnectionView!!.connector.connectionType} выступил как получатель")
        }


        if(nearestBlock == null || sourceDragBlock.outputConnectionView == null || nearestConnection == null) return

        if(getLengthFromConnections(sourceDragBlock, nearestBlock, sourceDragBlock.outputConnectionView!!, nearestConnection, density) <= connetionLength){
            safeConnect(sourceDragBlock.outputConnectionView!!.connector, nearestConnection.connector)


            val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
            val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
            val nearestConX = with(density) {nearestConnection.positionX.toPx()}
            val nearestConY = with(density) {nearestConnection.positionY.toPx()}

            viewModel.updateBlockPosition(
                sourceDragBlock,
                nearestBlock.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                nearestBlock.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
            )
            sourceDragBlock.connectedParent = nearestBlock
            sourceDragBlock.connectedParentConnectionView = nearestConnection
            nearestBlock.scope.add(sourceDragBlock)
            Log.i("Connect", "${sourceDragBlock.block.blockType}")

        }
    }

    private fun parentDisconnectOrNot(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density): Boolean {
        sourceDragBlock.connectedParent?.let {
            if(sourceDragBlock.connectedParentConnectionView == null) {
                sourceDragBlock.connectedParent = null
                throw IllegalArgumentException("ConnectionView для ${sourceDragBlock.block.blockType} оказался null")
            }
            else if(getLengthFromConnections(sourceDragBlock, sourceDragBlock.connectedParent!!, sourceDragBlock.outputConnectionView!!, sourceDragBlock.connectedParentConnectionView!!, density) > connetionLength){
                disconnect(sourceDragBlock.outputConnectionView!!.connector, sourceDragBlock.connectedParentConnectionView!!.connector)

                sourceDragBlock.connectedParent!!.scope.remove(sourceDragBlock)

                sourceDragBlock.connectedParent = null
                sourceDragBlock.connectedParentConnectionView = null
                Log.i("Disconnect", "${sourceDragBlock.block.blockType}")
                return true
            }
            else{

                val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
                val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
                val nearestConX = with(density) {sourceDragBlock.connectedParentConnectionView!!.positionX.toPx()}
                val nearestConY = with(density) {sourceDragBlock.connectedParentConnectionView!!.positionY.toPx()}

                viewModel.updateBlockPosition(
                    sourceDragBlock,
                    sourceDragBlock.connectedParent!!.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                    sourceDragBlock.connectedParent!!.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
                )
                return false
            }
        }
        return true
    }

    private fun getLengthFromConnections(
        dragBlock1: DraggableBlock,
        dragBlock2: DraggableBlock,
        connection1: ConnectionView,
        connection2: ConnectionView,
        density: Density
    ): Double {

        val con1x = with(density) {connection1.positionX.toPx()}
        val con1y = with(density) {connection1.positionY.toPx()}
        val con2x = with(density) {connection2.positionX.toPx()}
        val con2y = with(density) {connection2.positionY.toPx()}

        val x1 = (dragBlock1.x.value + con1x)
        val y1 = (dragBlock1.y.value + con1y)
        val x2 = (dragBlock2.x.value + con2x)
        val y2 = (dragBlock2.y.value + con2y)

        return sqrt((x1 - x2).toDouble().pow(2) + (y1 - y2).toDouble().pow(2))
    }
    fun getBlockWithNearestConnection(dragBlock: DraggableBlock, listBlocks: MutableList<DraggableBlock>, density: Density, connectionType: ConnectorType, requiresVoid: Boolean): DraggableBlock? {

        var ans: DraggableBlock? = null

        var r = 1000000.0

        if(requiresVoid){
            for(block in listBlocks) {

                if(block.block !is VoidBlock) continue

                for(connection in block.inputConnectionViews){

                    if(connection.connector.connectionType != connectionType) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                    val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                    if( length < r){
                        r = length
                        ans = block
                    }
                }

            }
        }
        else {
            for(block in listBlocks) {

                for(connection in block.inputConnectionViews){

                    if(connection.connector.connectionType != connectionType) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                    val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                    if( length < r){
                        r = length
                        ans = block
                    }
                }
            }
        }

        return ans
    }
    fun getNearestConnection(dragBlock: DraggableBlock,listBlocks: MutableList<DraggableBlock>, density: Density, connectionType: ConnectorType): ConnectionView? {

        var ans: ConnectionView? = null

        var r = 1000000.0

        for(block in listBlocks) {

            for(connection in block.inputConnectionViews){

                if(connection.connector.connectionType != connectionType) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                if( length < r){
                    r = length
                    ans = connection
                }
            }
        }
        return ans
    }




    fun initConnectionsFromBlock(block: Block): MutableList<ConnectionView> {

        val ans: MutableList<ConnectionView> = mutableListOf()

        val cornerOffset = ViewInitialSize.cornerOffset

        val sizeOfBlock = ViewInitialSize.getInitialSizeByBlockType(block.blockType)
            ?: throw IllegalArgumentException("Для блока типа ${block.blockType} не заданы размеры")

        val width = sizeOfBlock.width
        val height = sizeOfBlock.height

        when(block.blockType) {
            BlockType.START -> {
                val castedBlock = (block as StartBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.bottomConnector, 0.dp, height),
                    ConnectionView(castedBlock.topConnector, width/10, 0.dp)
                )
            }
            BlockType.OPERAND -> {
                val castedBlock = (block as OperandBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2),
                )
            }
            BlockType.SET_VARIABLE_VALUE -> {
                val castedBlock = (block as SetValueVariableBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.VARIABLE_DECLARATION -> {
                val castedBlock = (block as VariableDeclarationBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueInputConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.INT_LITERAL -> {
                val castedBlock = (block as IntLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_LITERAL -> {
                val castedBlock = (block as StringLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.BOOLEAN_LITERAL -> {
                val castedBlock = (block as BooleanLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.VARIABLE_REFERENCE -> {
                val castedBlock = (block as VariableReferenceBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_CONCAT -> {
                val castedBlock = (block as StringConcatenationBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2),
                )
            }
            BlockType.STRING_APPEND -> {
                val castedBlock = (block as StringAppendBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.PRINT_BLOCK -> {
                val castedBlock = (block as PrintBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.IF_BLOCK -> {
                val castedBlock = (block as IfBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, width/2, height),
                    ConnectionView(castedBlock.conditionConnector, width, height/2)
                )
            }
            else -> {}
        }
        return ans
    }
}