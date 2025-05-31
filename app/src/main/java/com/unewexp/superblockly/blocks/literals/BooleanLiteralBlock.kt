package com.unewexp.superblockly.blocks.literals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class BooleanLiteralBlock(var initialValue: Boolean = false) : Block(UUID.randomUUID(), BlockType.BOOLEAN_LITERAL) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )

    var value by mutableStateOf(initialValue)

    override suspend fun evaluate(): Boolean{
        checkDebugPause()
        return value
    }
}