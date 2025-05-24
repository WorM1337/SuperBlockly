package com.unewexp.superblockly.viewBlocks

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.model.ConnectorManager

data class DraggableBlock(
    val id: String,
    val block: Block,
    var x: MutableState<Float>,
    var y: MutableState<Float>,
    var outputConnectionView: ConnectionView? = null,
    var inputConnectionViews: MutableList<ConnectionView> = mutableListOf(),
    val scope: MutableList<DraggableBlock> = mutableListOf(),
    var width: MutableState<Dp> = mutableStateOf(100.dp),
    var height: MutableState<Dp> = mutableStateOf(60.dp),
    var connectedParent: DraggableBlock? = null,
    var connectedParentConnectionView: ConnectionView? = null,
    var isInner: Boolean = false // Показывает, находится ли блок внутри другого блока. Например параметры для OperandBlock, либо поле для void-блоков для if
){
    init {

        val sizeOfBlock = ViewInitialSize.getInitialSizeByBlockType(block.blockType)

        if(sizeOfBlock == null) throw IllegalArgumentException("Ошибка инициализации блока")
        width.value = sizeOfBlock.width
        height.value = sizeOfBlock.height

        val connectionViews = ConnectorManager.initConnectionsFromBlock(block)

        connectionViews.forEach{
            if(it.connector.connectionType == ConnectorType.OUTPUT || it.connector.connectionType == ConnectorType.STRING_TOP){
                outputConnectionView = it
            }
            else {
                inputConnectionViews.add(it)
            }
        }
        if(outputConnectionView == null){
            inputConnectionViews = mutableListOf()
            throw IllegalArgumentException("Ошибка отрисовки коннекторов для блока ${this.id}")
        }
    }
}
