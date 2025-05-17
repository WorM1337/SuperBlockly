package com.unewexp.superblockly

import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class DraggableViewModel: ViewModel() {
    private val _blocks = MutableStateFlow<List<DraggableBlock>>(emptyList())
    val blocks = _blocks.asStateFlow()

    private val _inputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _outputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())

    fun addBlock(block: DraggableBlock) {
        _blocks.value = _blocks.value + block
    }

    fun updateBlockPosition(id: String, newX: Float, newY: Float) {
        _blocks.value = _blocks.value.map { block ->
            if (block.id == id) block.copy(x = newX, y = newY) else block
        }
    }

    fun removeBlock(id: String) {
        _blocks.value = _blocks.value.filter { it.id != id }
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
            }
        }
    }
}