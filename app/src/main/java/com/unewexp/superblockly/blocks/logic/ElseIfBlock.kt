package com.unewexp.superblockly.blocks.logic

import com.unewexp.superblockly.enums.BlockType
import java.util.UUID

class ElseIfBlock : ConditionBlock(UUID.randomUUID(), BlockType.IF_ELSE_BLOCK) {

    override suspend fun execute() {
        checkDebugPause()
        val condition = conditionConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Выражение не возвращает Boolean")

        executeConditionBlock(condition)
    }
}