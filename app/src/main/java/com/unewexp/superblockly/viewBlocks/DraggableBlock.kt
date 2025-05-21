package com.unewexp.superblockly.viewBlocks

import androidx.compose.runtime.MutableState
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
    var width: Dp = 100.dp,
    var height: Dp = 60.dp,
){
    init {

        if(!ViewInitialSize.sizeDictionary.containsKey(block.blockType)) throw IllegalArgumentException("Ошибка инициализации блока")

        width = ViewInitialSize.sizeDictionary[block.blockType]!!.x
        height = ViewInitialSize.sizeDictionary[block.blockType]!!.y

        val connectionViews = ConnectorManager.initConnectionsFromBlock(block)

        connectionViews.forEach{
            if(it.connector.connectionType == ConnectorType.OUTPUT){
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
