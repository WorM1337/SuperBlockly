package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class GetListSize : Block(UUID.randomUUID(), BlockType.GET_LIST_SIZE) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this
    )

    val listConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.FIXED_VALUE_AND_SIZE_LIST
        )
    )

    override suspend fun evaluate(): Any? {
        checkDebugPause()

        val list = listConnector.connectedTo?.evaluate() as? List<*>
            ?: throw IllegalStateException("Переданная переменная не содержит список")

        return list.size
    }
}