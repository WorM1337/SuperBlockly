package com.unewexp.superblockly.blocks.literals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class StringLiteralBlock(var initialValue: String = "Empty String") : Block(UUID.randomUUID(), BlockType.STRING_LITERAL) {

    var value by mutableStateOf(initialValue)

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this
    )

    override suspend fun evaluate(): String {
        checkDebugPause()
        return value
    }
}