package com.unewexp.superblockly.blocks.logic

import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.ErrorHandler
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID

class ElseBlock : ConditionBlock(UUID.randomUUID(), BlockType.ELSE_BLOCK) {

    override fun execute() {
        executeInnerBlocks()
    }
}