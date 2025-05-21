package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.utils.canConnect
import com.example.myfirstapplicatioin.utils.safeConnect
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.returnBlocks.OperandBlock
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
import java.lang.Math.pow
import kotlin.coroutines.coroutineContext
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object ConnectorManager {
    val connetionLength = 50.0


    fun tryConnectDrag(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density){

        val listDragBlocks = viewModel.blocks.value.filter { it !in sourceDragBlock.scope && (it != sourceDragBlock) }.toMutableList()

        val nearestBlock = getBlockWithNearestConnection(sourceDragBlock, listDragBlocks, density)
        val nearestConnection = getNearestConnection(sourceDragBlock, listDragBlocks, density)

        if(nearestBlock == null || sourceDragBlock.outputConnectionView == null || nearestConnection == null) return

        if(getLengthFromConnections(sourceDragBlock, nearestBlock, sourceDragBlock.outputConnectionView!!, nearestConnection, density) <= connetionLength){
            safeConnect(sourceDragBlock.outputConnectionView!!.connector, nearestConnection.connector)


            val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
            val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
            val nearestConX = with(density) {nearestConnection.positionX.toPx()}
            val nearestConY = with(density) {nearestConnection.positionY.toPx()}

            viewModel.updateBlockPosition(
                sourceDragBlock.id,
                nearestBlock.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                nearestBlock.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
            )
            nearestBlock.scope.add(sourceDragBlock)
            Log.i("Connect", "${sourceDragBlock.block.blockType}")

        }
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
    fun getBlockWithNearestConnection(dragBlock: DraggableBlock, listBlocks: MutableList<DraggableBlock>, density: Density): DraggableBlock? {

        var ans: DraggableBlock? = null

        var r = 1000000.0

        for(block in listBlocks) {

            for(connection in block.inputConnectionViews){
                val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                if( length < r){
                    r = length
                    ans = block
                }
            }
        }
        return ans
    }
    fun getNearestConnection(dragBlock: DraggableBlock,listBlocks: MutableList<DraggableBlock>, density: Density): ConnectionView? {

        var ans: ConnectionView? = null

        var r = 1000000.0

        for(block in listBlocks) {

            for(connection in block.inputConnectionViews){
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

        when(block.blockType) {
            BlockType.START -> {}
            BlockType.OPERAND -> {
                val castedBlock = (block as OperandBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.OPERAND]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.OPERAND]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2),
                )
            }
            BlockType.SET_VARIABLE_VALUE -> {
                val castedBlock = (block as SetValueVariableBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.SET_VARIABLE_VALUE]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.SET_VARIABLE_VALUE]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.VARIABLE_DECLARATION -> {
                val castedBlock = (block as VariableDeclarationBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_DECLARATION]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_DECLARATION]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueInputConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.INT_LITERAL -> {
                val castedBlock = (block as IntLiteralBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_LITERAL -> {
                val castedBlock = (block as StringLiteralBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.BOOLEAN_LITERAL -> {
                val castedBlock = (block as BooleanLiteralBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.BOOLEAN_LITERAL]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.BOOLEAN_LITERAL]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.VARIABLE_REFERENCE -> {
                val castedBlock = (block as VariableReferenceBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_REFERENCE]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_REFERENCE]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_CONCAT -> {
                val castedBlock = (block as StringConcatenationBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.STRING_CONCAT]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.STRING_CONCAT]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2),
                )
            }
            BlockType.STRING_APPEND -> { // Пока что неактуальная версия - Кирилл уже переписал. Нужно будет залить и дописать
//                val castedBlock = (block as StringAppendBlock)
//                ans += mutableListOf(
//                    castedBlock.stringToAppendConnector,
//                    castedBlock.variableInputConnector
//                )
            }
            BlockType.PRINT_BLOCK -> {
                val castedBlock = (block as PrintBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.SET_VARIABLE_VALUE]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.SET_VARIABLE_VALUE]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            else -> {}
        }
        return ans
    }
}