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
    open val topConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.START,
            BlockType.STRING_APPEND,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.VARIABLE_DECLARATION,
            BlockType.PRINT_BLOCK,
            BlockType.IF_BLOCK,
            BlockType.ELSE_BLOCK,
            BlockType.IF_ELSE_BLOCK,
        )
    )

    open val bottomConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.IF_BLOCK,
            BlockType.STRING_APPEND,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.PRINT_BLOCK,
            BlockType.VARIABLE_DECLARATION,
        )
    )

    fun getNextBlock(): Block?{
        return bottomConnector.connectedTo
    }



}