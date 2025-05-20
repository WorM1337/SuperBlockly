package com.unewexp.superblockly

import android.util.Log
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
import kotlinx.coroutines.flow.update
import java.util.UUID
import kotlin.math.abs

class DraggableViewModel: ViewModel() {

    private val _inputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _outputConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _topConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())
    private val _bottomConnectors = MutableStateFlow<MutableMap<UUID, ConnectionView>>(mutableMapOf())

    private val _blocks = MutableStateFlow<MutableList<DraggableBlock>>(mutableListOf())
    val blocks = _blocks.asStateFlow()

    fun addBlock(dragBlock: DraggableBlock) {
        _blocks.update {
            (_blocks.value.toList() + dragBlock).toMutableList()
        }
    }

    fun updateBlockPosition(id: String, offsetX: Float, offsetY: Float) {
        val currentBlock = findBlockById(id)
        currentBlock?.scope?.forEach {
            //if (abs(offsetX) < 1.0 && abs(offsetY) < 1.0) return
            updateBlockPosition(it.id, offsetX, offsetY)
        }
        currentBlock?.let {
            currentBlock.x = currentBlock.x + offsetX
            currentBlock.y = currentBlock.y + offsetY
            Log.i("${currentBlock.block.blockType}", "(${currentBlock.x} : ${currentBlock.y})")
        }
        _blocks.update {
            _blocks.value.map { block ->
                if (block.id == id) block else block
            }.toMutableList()
        }
    }

    fun updateBlockPositionByXY(id: String, newX: Float, newY: Float) {

        val currentBlock = findBlockById(id)
        currentBlock?.let{
            val offsetX = newX - currentBlock.x
            val offsetY = newY - currentBlock.y
            currentBlock.x = newX
            currentBlock.y = newY
            Log.i("${currentBlock.block.blockType}", "(${currentBlock.x} : ${currentBlock.y})")
            currentBlock.scope.forEach {
                if(abs(offsetX) < 1.0 && abs(offsetY) < 1.0) return
                updateBlockPosition(it.id, offsetX, offsetY)
            }
        }

    }

    fun removeBlock(id: String) {
        val block = findBlockById(id)
        if(block == null){
            return
        }
        block.scope.forEach {
            removeBlock(it.id)
        }
        _blocks.update {
            (_blocks.value.filter { it != block }).toMutableList()
        }
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