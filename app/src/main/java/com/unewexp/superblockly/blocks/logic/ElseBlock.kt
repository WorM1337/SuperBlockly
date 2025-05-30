package com.unewexp.superblockly.blocks.logic

import com.unewexp.superblockly.enums.BlockType
import java.util.UUID

class ElseBlock : ConditionBlock(UUID.randomUUID(), BlockType.ELSE_BLOCK) {

    override suspend fun execute() {
        checkDebugPause()
        executeInnerBlocks()
    }
}