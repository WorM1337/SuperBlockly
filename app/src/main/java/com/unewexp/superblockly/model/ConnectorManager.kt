package com.unewexp.superblockly.model

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
import kotlin.math.sqrt

object ConnectorManager {
    val connetionLength = 30.0


    fun tryConnectDrag(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel) : Boolean{
        val nearestBlock = getBlockWithNearestConnection(sourceDragBlock, viewModel)
        val nearestConnection = getNearestConnection(sourceDragBlock, viewModel)

        if(nearestBlock == null || sourceDragBlock.outputConnectionView == null || nearestConnection == null) return false

        if(getLengthFromConnections(sourceDragBlock, nearestBlock, sourceDragBlock.outputConnectionView!!, nearestConnection) <= connetionLength){
            safeConnect(sourceDragBlock.outputConnectionView!!.connector, nearestConnection.connector)
            return true
        }
        return false
    }


    private fun getLengthFromConnections(
        dragBlock1: DraggableBlock,
        dragBlock2: DraggableBlock,
        connection1: ConnectionView,
        connection2: ConnectionView,
    ): Double {
        val x1 = dragBlock1.x.toDouble() + connection1.positionX.value.toDouble()
        val y1 = dragBlock1.y.toDouble() + connection1.positionY.value.toDouble()
        val x2 = dragBlock2.x.toDouble() + connection2.positionX.value.toDouble()
        val y2 = dragBlock2.y.toDouble() + connection2.positionY.value.toDouble()

        return sqrt(pow(x1-x2,2.0) + pow(y1-y2, 2.0))
    }
    fun getBlockWithNearestConnection(dragBlock: DraggableBlock, viewModel: DraggableViewModel): DraggableBlock? {

        var ans: DraggableBlock? = null

        var r = 1000000.0

        for(block in viewModel.blocks.value) {

            for(connection in block.inputConnectionViews){
                val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection)
                if( length < r && canConnect(dragBlock.outputConnectionView!!.connector, connection.connector)){
                    r = length
                    ans = block
                }
            }
        }
        return ans
    }
    fun getNearestConnection(dragBlock: DraggableBlock, viewModel: DraggableViewModel): ConnectionView? {

        var ans: ConnectionView? = null

        var r = 1000000.0

        for(block in viewModel.blocks.value) {

            for(connection in block.inputConnectionViews){
                val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection)
                if( length < r && canConnect(dragBlock.outputConnectionView!!.connector, connection.connector)){
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
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
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
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
                )
            }
            BlockType.STRING_LITERAL -> {
                val castedBlock = (block as StringLiteralBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.INT_LITERAL]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
                )
            }
            BlockType.BOOLEAN_LITERAL -> {
                val castedBlock = (block as BooleanLiteralBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.BOOLEAN_LITERAL]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.BOOLEAN_LITERAL]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
                )
            }
            BlockType.VARIABLE_REFERENCE -> {
                val castedBlock = (block as VariableReferenceBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_REFERENCE]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.VARIABLE_REFERENCE]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
                )
            }
            BlockType.STRING_CONCAT -> {
                val castedBlock = (block as StringConcatenationBlock)

                val width = ViewInitialSize.sizeDictionary[BlockType.STRING_CONCAT]!!.x
                val height = ViewInitialSize.sizeDictionary[BlockType.STRING_CONCAT]!!.y

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/3),
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