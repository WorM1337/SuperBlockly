package com.unewexp.superblockly.blocks

import android.os.Debug
import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.debug.DebugController
import com.unewexp.superblockly.debug.ErrorHandler
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.debug.Logger
import com.unewexp.superblockly.debug.RunProgram
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID

class StartBlock : VoidBlock(UUID.randomUUID(), BlockType.START) {

    override suspend fun execute() {

        Logger.clearLogs()
        ExecutionContext.clearVariables()
        ErrorHandler.clearBlockErrors()

        try{
            getNextBlock()?.let { firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                var current: Block? = firstBlock
                while (current != null) {
                    try {
                        current.execute()
                    } catch (e: Exception) {
                        ErrorHandler.setBlockError(current, e.message ?: "Неизвестная ошибка")
                        break
                    }
                    current = ExecutionContext.getNextBlockInScope()
                }

            }
        } finally{
            ExecutionContext.exitCurrentScope()
            Logger.markFinished()
            DebugController.reset()
            ExecutionContext.programProgress = RunProgram.NONE
        }


    }
}