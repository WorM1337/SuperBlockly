package com.example.myfirstapplicatioin.blocks.literals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class IntLiteralBlock(var initialValue: Int = 123 ) : Block(UUID.randomUUID(), BlockType.INT_LITERAL) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )

    var value by mutableIntStateOf(initialValue)

    override suspend fun evaluate(): Int {
        checkDebugPause()
        return value
    }
}