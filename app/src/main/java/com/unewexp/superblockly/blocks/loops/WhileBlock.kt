package com.unewexp.superblockly.blocks.loops

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class WhileBlock : LoopBlock(UUID.randomUUID(), BlockType.WHILE_BLOCK) {

    val conditionConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.BOOLEAN_LITERAL,
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.NOT_BLOCK
        )
    )

    override suspend fun execute() {
        checkDebugPause()
        while (
            conditionConnector.connectedTo?.evaluate() as? Boolean
                ?: throw BlockIllegalStateException(this, "Выражение в цикле while не возвращает Boolean")
        ) {
            checkDebugPause()
            (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                executeInnerBlocks(firstBlock)
            }
        }
    }
}