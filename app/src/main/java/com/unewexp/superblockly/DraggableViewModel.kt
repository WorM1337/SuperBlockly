package com.unewexp.superblockly

import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.utils.disconnect
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import kotlin.math.abs

class DraggableViewModel: ViewModel() {

    private val _blocks = MutableStateFlow<List<DraggableBlock>>(listOf())
    val blocks = _blocks.asStateFlow()

    fun addBlock(dragBlock: DraggableBlock) {
        _blocks.update {
            (_blocks.value + dragBlock)
        }
    }

    fun updateBlockPosition(id: String, offsetX: Float, offsetY: Float) {
        val currentBlock = findBlockById(id)
        currentBlock?.scope?.forEach {
            updateBlockPosition(it.id, offsetX, offsetY)
        }
        currentBlock?.let {
            currentBlock.x.value = currentBlock.x.value + offsetX
            currentBlock.y.value = currentBlock.y.value + offsetY
            Log.i("${currentBlock.block.blockType}", "(${currentBlock.x.value} : ${currentBlock.y.value})")
        }
    }

    fun removeBlock(id: String) {
        val block = findBlockById(id)

        if(block == null){
            return
        }

        for(i in block.scope.indices.reversed()){
            removeBlock(block.scope[i].id)
        }

        if(block.connectedParent != null && block.connectedParentConnectionView != null){
            disconnect(block.outputConnectionView!!.connector, block.connectedParentConnectionView!!.connector)
            block.connectedParent!!.scope.remove(block)
            block.connectedParent = null
            block.connectedParentConnectionView = null
        }

        _blocks.update {
            _blocks.value.filter {
                Log.i(it.block.blockType.name, it.id)
                it != block
            }
        }
        Log.i("Delete", "${block.block.blockType} with id: " + block.id)
    }

    fun findBlockById(id: String): DraggableBlock?{
        _blocks.value.forEach { block ->
            if (block.id == id){
                return block
            }
        }
        return null
    }

    fun updateValue(id: String, newValue: String){
        _blocks.value.forEach {
            if(it.id == id){
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