package com.example.myfirstapplicatioin.blocks.literals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class IntLiteralBlock() : Block(UUID.randomUUID(), BlockType.INT_LITERAL) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.VARIABLE_DECLARATION,
            BlockType.SET_VARIABLE_VALUE
        ),
        allowedDataTypes = setOf(Int::class.java)
        // Int::class.java - означает, что тут можно использовать только тип данных int
    )

    var value by mutableStateOf(123)

    override fun evaluate(): Int = value
}