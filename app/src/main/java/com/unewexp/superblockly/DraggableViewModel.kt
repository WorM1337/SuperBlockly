package com.unewexp.superblockly

import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.utils.disconnect
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.loops.ForBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.model.CalculationsManager
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.model.SizeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min

class DraggableViewModel: ViewModel() {

    private val _blocks = MutableStateFlow<List<DraggableBlock>>(listOf())
    val blocks = _blocks.asStateFlow()
    var maxZIndex = 0f;

    var density: Density = Density(0.0f)

    fun handleAction(action: BlocklyAction) {
        when(action) {
            is BlocklyAction.MoveBlock -> updateBlockPosition(
                action.block,
                action.offsetX,
                action.offsetY
            )
            is BlocklyAction.AddBlock -> addBlock(action.block)
            is BlocklyAction.RemoveBlock -> removeBlock(action.block)
        }
    }

    private fun addBlock(dragBlock: DraggableBlock) {
        dragBlock.zIndex.value = maxZIndex
        _blocks.update {
            (_blocks.value + dragBlock)
        }
        maxZIndex += 0.1f
    }

    private fun updateBlockPosition(currentBlock: DraggableBlock, offsetX: Float, offsetY: Float) {
        currentBlock.scope.forEach {
            updateBlockPosition(it, offsetX, offsetY)
        }

        currentBlock.x.value = currentBlock.x.value + offsetX
        currentBlock.y.value = currentBlock.y.value + offsetY
    }

    private fun removeBlock(block: DraggableBlock, isFirst: Boolean = true) {

        if(block.block is StartBlock){
            return
        }

        var sumHeight = 0.dp

        if(isFirst) sumHeight = CalculationsManager.getSummaryHeight(block)

        val child = ConnectorManager.getStringBottomOuterConnectionChild(block)
        val parent = block.connectedParent
        val connectionParent = block.connectedParentConnectionView

        for(i in block.scope.indices.reversed()){

            if(block.scope[i].connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_OUTER || block.scope[i].isInner && !isFirst){
                removeBlock(block.scope[i], false)
            }
            else {
                disconnect(block.scope[i].outputConnectionView!!.connector, block.scope[i].connectedParentConnectionView!!.connector)

                block.scope[i].connectedParentConnectionView!!.isConnected = false
                block.scope[i].connectedParent = null
                block.scope[i].connectedParentConnectionView = null
                block.scope[i].outputConnectionView!!.isConnected = false
                block.scope.removeAt(i)
            }
        }

        if(block.connectedParent != null && block.connectedParentConnectionView != null){
            disconnect(block.outputConnectionView!!.connector, block.connectedParentConnectionView!!.connector)
            if(isFirst){
                if(sumHeight > block.height.value){
                    SizeManager.changeParentParams(block, this, deltaHeight = sumHeight, isPositive = false)
                }
                else {
                    SizeManager.changeParentParams(block, this, isPositive = false)
                }
            }



            block.connectedParent!!.scope.remove(block)
            block.connectedParentConnectionView!!.isConnected = false
            block.outputConnectionView!!.isConnected = false
            block.connectedParent = null
            block.connectedParentConnectionView = null

            if(isFirst && child != null) {
                ConnectorManager.tryConnectBlocks(child,parent!!,connectionParent!!,this, density)
            }
        }

        _blocks.update {
            _blocks.value.filter {
                it != block
            }
        }
        Log.i("Delete", "${block.block.blockType} with id: " + block.block.id)
    }

    fun updateValue(block: DraggableBlock, newValue: String): Boolean{
        val realBlock = block.block
        when(realBlock.blockType){
            BlockType.SET_VARIABLE_VALUE -> {
                (realBlock as SetValueVariableBlock).selectedVariableName = newValue
                return newValue.matches(Regex("[_a-zA-Z]\\w*"))
            }
            BlockType.START -> TODO()
            BlockType.INT_LITERAL -> {
                if(newValue.length > 5){
                    block.width.value = 100.dp + (newValue.length - 5)*10.dp
                }
                val v = newValue.toFloatOrNull()
                if(v != null){
                    (realBlock as IntLiteralBlock).value = v.toInt()
                    return true
                }else{
                    return false
                }
            }
            BlockType.STRING_LITERAL -> TODO()
            BlockType.BOOLEAN_LITERAL -> TODO()
            BlockType.OPERAND -> TODO()
            BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
            BlockType.VARIABLE_DECLARATION -> {
                (realBlock as VariableDeclarationBlock).name = newValue
                return newValue.matches(Regex("[_a-zA-Z]\\w*"))
            }
            BlockType.VARIABLE_REFERENCE -> {
                (realBlock as VariableReferenceBlock).selectedVariableName = newValue
                return newValue.matches(Regex("[_a-zA-Z]\\w*"))
            }
            BlockType.STRING_CONCAT -> TODO()
            BlockType.STRING_APPEND -> TODO()
            BlockType.PRINT_BLOCK -> TODO()
            BlockType.COMPARE_NUMBERS_BLOCK -> TODO()
            BlockType.BOOLEAN_LOGIC_BLOCK -> TODO()
            BlockType.NOT_BLOCK -> TODO()
            BlockType.IF_BLOCK -> TODO()
            BlockType.ELSE_BLOCK -> TODO()
            BlockType.IF_ELSE_BLOCK -> TODO()
            BlockType.REPEAT_N_TIMES -> TODO()
            BlockType.WHILE_BLOCK -> TODO()
            BlockType.FOR_BLOCK -> {
                (realBlock as ForBlock).variableName = newValue
                return newValue.matches(Regex("[_a-zA-Z]\\w*"))
            }
            BlockType.FOR_ELEMENT_IN_LIST -> TODO()
            BlockType.FIXED_VALUE_AND_SIZE_LIST -> TODO()
            BlockType.GET_VALUE_BY_INDEX -> TODO()
            BlockType.REMOVE_VALUE_BY_INDEX -> TODO()
            BlockType.ADD_VALUE_BY_INDEX -> TODO()
            BlockType.GET_LIST_SIZE -> TODO()
            BlockType.EDIT_VALUE_BY_INDEX -> TODO()
            BlockType.PUSH_BACK_ELEMENT -> TODO()
        }
    }

    fun normalizeZIndex() {
        var minZIndex = maxZIndex
        blocks.value.forEach {
            minZIndex = min(minZIndex, it.zIndex.value)
        }
        if(minZIndex > 0){
            _blocks.value.forEach {
                it.zIndex.value -= minZIndex
            }
        }
    }

    sealed class BlocklyAction{
        data class MoveBlock(var block: DraggableBlock, var offsetX: Float, var offsetY: Float): BlocklyAction()
        data class AddBlock(var block: DraggableBlock): BlocklyAction()
        data class RemoveBlock(var block: DraggableBlock): BlocklyAction()
    }
}