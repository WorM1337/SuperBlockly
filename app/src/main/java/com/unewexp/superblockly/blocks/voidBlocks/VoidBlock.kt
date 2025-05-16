package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID


open class VoidBlock(
    id: UUID = UUID.randomUUID(),
    blockType: BlockType
) : Block(id, blockType) {
    val topConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(BlockType.START, BlockType.VOID_BLOCK)
    )

    val bottomConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(BlockType.VOID_BLOCK)
    )

    fun getNextBlock(): Block?{
        return bottomConnector.connectedTo
    }
}