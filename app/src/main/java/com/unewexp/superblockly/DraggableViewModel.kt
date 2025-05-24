package com.unewexp.superblockly

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.utils.disconnect
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DraggableViewModel: ViewModel() {

    private val _blocks = MutableStateFlow<List<DraggableBlock>>(listOf())
    val blocks = _blocks.asStateFlow()

    fun addBlock(dragBlock: DraggableBlock) {
        _blocks.update {
            (_blocks.value + dragBlock)
        }
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

        for(i in block.scope.indices.reversed()){

            if(block.scope[i].outputConnectionView!!.connector.connectionType == ConnectorType.STRING_TOP){
                // Если при просмотре детей оказалось, что этот коннектор - верхний у void блока, то этот блок мы удалять не должны
                block.scope.removeAt(i)
            }
            else {
                removeBlock(block.scope[i])
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
        Log.i("Delete", "${block.block.blockType} with id: " + block.id)
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
}