package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class StartBlock : Block(UUID.randomUUID(), BlockType.START) {
    val nextBlockConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(BlockType.VOID_BLOCK)
    )
}