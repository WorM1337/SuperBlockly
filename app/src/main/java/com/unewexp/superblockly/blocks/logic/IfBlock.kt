package com.unewexp.superblockly.blocks.logic


import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID




class IfBlock : ConditionBlock(UUID.randomUUID(), BlockType.IF_BLOCK) {
    override suspend fun execute() {
        checkDebugPause()
        val condition = conditionConnector.connectedTo?.evaluate() as? Boolean
            ?: throw BlockIllegalStateException(this, "If должен получать на вход Boolean")

        executeConditionBlock(condition)

    }
}