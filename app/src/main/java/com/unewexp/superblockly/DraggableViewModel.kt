package com.unewexp.superblockly

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.utils.disconnect
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.ConnectorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min

class DraggableViewModel: ViewModel() {

    private val _blocks = MutableStateFlow<List<DraggableBlock>>(listOf())
    val blocks = _blocks.asStateFlow()
    var maxZIndex = 0f;

    fun addBlock(dragBlock: DraggableBlock) {
        _blocks.update {
            (_blocks.value + dragBlock)
        }
        maxZIndex += 0.1f
    }

    fun updateBlockPosition(currentBlock: DraggableBlock, offsetX: Float, offsetY: Float) {
        currentBlock.scope.forEach {
            updateBlockPosition(it, offsetX, offsetY)
        }

        currentBlock.x.value = currentBlock.x.value + offsetX
        currentBlock.y.value = currentBlock.y.value + offsetY
        Log.i("${currentBlock.block.blockType}", "(${currentBlock.x.value} : ${currentBlock.y.value})")
    }

    fun removeBlock(block: DraggableBlock) {

        if(block.block is StartBlock){
            return
        }

        for(i in block.scope.indices.reversed()){

            if(block.scope[i].connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_OUTER || block.scope[i].isInner){
                removeBlock(block.scope[i])
            }
            else {
                block.scope.removeAt(i)
            }
        }

        if(block.connectedParent != null && block.connectedParentConnectionView != null){
            disconnect(block.outputConnectionView!!.connector, block.connectedParentConnectionView!!.connector)
            block.connectedParent!!.scope.remove(block)
            block.connectedParent = null
            block.connectedParentConnectionView = null
        }

        _blocks.update {
            _blocks.value.filter {
                it != block
            }
        }
        Log.i("Delete", "${block.block.blockType} with id: " + block.block.id)
    }

    fun updateValue(block: DraggableBlock, newValue: String){
        _blocks.value.forEach {
            if(it == block){
                if(it.block is IntLiteralBlock){
                    val v = newValue.toFloatOrNull()
                    if(v != null){
                        it.block.value = v.toInt()
                    }else{
                        it.block.value = 0
                    }
                }
                if(it.block is SetValueVariableBlock){
                    it.block.selectedVariableName = newValue
                }
                if(it.block is VariableDeclarationBlock){
                    it.block.name = newValue
                }
                if(it.block is VariableReferenceBlock){
                    it.block.selectedVariableName = newValue
                }
            }
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
}