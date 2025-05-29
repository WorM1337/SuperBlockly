package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

open class ListBlock(
    id: UUID,
    blockType: BlockType
) : Block(id, blockType) {

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )


}