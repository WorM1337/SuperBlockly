package com.unewexp.superblockly.blocks.returnBlocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class StringConcatenationBlock : Block(UUID.randomUUID(), BlockType.STRING_CONCAT) {
    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(String::class.java, Int::class.java, Boolean::class.java)
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(String::class.java, Int::class.java, Boolean::class.java)
    )

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,

    )

    override suspend fun evaluate(): String {

        checkDebugPause()
        val left = leftInputConnector.connectedTo?.evaluate()?.toString()
            ?: throw BlockIllegalStateException(this, "Не указана левая строка")

        val right = rightInputConnector.connectedTo?.evaluate()?.toString()
            ?: throw BlockIllegalStateException(this, "Не указана правая строка")
        return left + right
    }
}