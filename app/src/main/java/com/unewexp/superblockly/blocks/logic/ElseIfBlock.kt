package com.unewexp.superblockly.blocks.logic

import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.ErrorHandler
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID
import kotlin.collections.contains

class ElseIfBlock : ConditionBlock(UUID.randomUUID(), BlockType.IF_ELSE_BLOCK) {

    override fun execute() {
        val condition = conditionConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Выражение не возвращает Boolean")

        executeConditionBlock(condition)
    }
}