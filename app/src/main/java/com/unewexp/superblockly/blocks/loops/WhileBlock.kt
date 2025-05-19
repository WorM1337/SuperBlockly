package com.unewexp.superblockly.blocks.loops

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
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

    override fun execute() {
        while (
            conditionConnector.connectedTo?.evaluate() as? Boolean
                ?: throw IllegalStateException("Выражение не возвращает Boolean")
        ) {
            (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                executeInnerBlocks(firstBlock)
            }
        }
    }
}