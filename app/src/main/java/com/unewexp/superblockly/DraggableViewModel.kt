package com.unewexp.superblockly

import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DraggableViewModel: ViewModel() {
    private val _blocks = MutableStateFlow<List<DraggableBlock>>(emptyList())
    val blocks = _blocks.asStateFlow()

    init {
        _blocks.value = listOf(
            DraggableBlock(id = "3", IntLiteralBlock(), x = 100f, y = 100f),
            DraggableBlock(id = "2", IntLiteralBlock(), x = 300f, y = 200f)
        )
    }

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
            if(it.id == id && it.block is IntLiteralBlock){
                val v = newValue.toFloatOrNull()
                if(v != null){
                    it.block.value = v.toInt()
                }
            }
        }
    }
}