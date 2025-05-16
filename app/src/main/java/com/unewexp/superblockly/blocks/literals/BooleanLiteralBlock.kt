package com.unewexp.superblockly.blocks.literals

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class BooleanLiteralBlock(var value: Boolean = false) : Block(UUID.randomUUID(), BlockType.BOOLEAN_LITERAL) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_DECLARATION
        ),
        allowedDataTypes = setOf(Boolean::class.java)
    )



    override fun evaluate(): Boolean = value
}