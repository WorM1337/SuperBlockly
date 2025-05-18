package com.unewexp.superblockly.viewBlocks

import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.model.ConnectorManager

data class DraggableBlock(
    val id: String,
    val block: Block,
    var x: Float,
    var y: Float,
    var outputConnectionView: ConnectionView? = null,
    var inputConnectionViews: MutableList<ConnectionView> = mutableListOf(),
    var width: Int = 100,
    var height: Int = 60,
){
    init {

        if(!ViewInitialSize.sizeDictionary.containsKey(block.blockType)) throw IllegalArgumentException("Ошибка инициализации блока")

        width = ViewInitialSize.sizeDictionary[block.blockType]!!.x.value.toInt()
        height = ViewInitialSize.sizeDictionary[block.blockType]!!.y.value.toInt()

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
