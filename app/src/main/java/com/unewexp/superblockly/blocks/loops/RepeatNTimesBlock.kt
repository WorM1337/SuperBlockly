package com.unewexp.superblockly.blocks.loops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID

class RepeatNTimesBlock(var initialCountRepeatTimes: Int = 10) : LoopBlock(UUID.randomUUID(), BlockType.REPEAT_N_TIMES) {

    var countRepeatTimes by mutableIntStateOf(initialCountRepeatTimes)

    override fun execute() {
        for (i in 1..countRepeatTimes){
            (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                executeInnerBlocks(firstBlock)
            }
        }
    }
}