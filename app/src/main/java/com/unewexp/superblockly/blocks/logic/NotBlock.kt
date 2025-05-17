package com.unewexp.superblockly.blocks.logic

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class NotBlock : Block(UUID.randomUUID(), BlockType.NOT_BLOCK) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
//        allowedDataTypes = setOf(Boolean::class.java)
    )

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.BOOLEAN_LITERAL,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.VARIABLE_REFERENCE
        )
    )

    override fun evaluate(): Boolean {
        val value = inputConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Соединение отсутствует или не Boolean")

        return !value
    }

}