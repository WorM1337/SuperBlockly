package com.unewexp.superblockly.blocks.logic

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class NotBlock : Block(UUID.randomUUID(), BlockType.NOT_BLOCK) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,

    )

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Boolean::class.java)
    )

    override suspend fun evaluate(): Boolean {
        checkDebugPause()
        val value = inputConnector.connectedTo?.evaluate() as? Boolean
            ?: throw BlockIllegalStateException(this, "Соединение отсутствует или не Boolean")

        return !value
    }

}