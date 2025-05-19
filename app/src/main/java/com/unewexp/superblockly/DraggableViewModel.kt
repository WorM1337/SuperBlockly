package com.unewexp.superblockly

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class DraggableViewModel: ViewModel() {
    private val _blocks = MutableStateFlow<List<DraggableBlock>>(emptyList())
    val blocks = _blocks.asStateFlow()

    private val _inputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _outputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _topConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _bottomConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())

    fun addBlock(dragBlock: DraggableBlock) {
        _blocks.value = _blocks.value + dragBlock
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