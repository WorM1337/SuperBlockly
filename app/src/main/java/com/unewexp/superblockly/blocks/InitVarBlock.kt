package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.ExecutionContext
import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.model.ConnectorType

class InitVarBlock(var name: String?) : VoidBlock() {

    val valueConnector = Connector(ConnectorType.INPUT, this)

    fun setConnectedBlock(block: Block) {
        valueConnector.connectedTo = block
    }


    override fun execution(context: ExecutionContext) {
        // Добавить инфу в дерево
    }

    override fun getInformationForTree(): MutableList<Block>? {

        if(valueConnector.sourceBlock != null && valueConnector.connectedTo != null) {
            return mutableListOf(valueConnector.sourceBlock,valueConnector.connectedTo!!)
        }
        // Либо по дефолту возвращать блок number со значением 0
        throw IllegalArgumentException("Отсутствие или неверное размещение блоков!")
    }
}