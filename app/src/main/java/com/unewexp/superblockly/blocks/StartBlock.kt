package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class StartBlock : VoidBlock(UUID.randomUUID(), BlockType.START) {

    override fun execute() {

        Logger.clearLogs()

        ExecutionContext.clearVariables()

        getNextBlock()?.let { firstBlock ->
            ExecutionContext.enterNewScope(firstBlock)
            try {
                var current: Block? = firstBlock
                while (current != null) {
                    try {
                        current.execute()
                    } catch (e: Exception) {
                        ErrorHandler.setBlockError(current.id, e.message ?: "Неизвестная ошибка")
                        current.hasException = true
                        break
                    }
                    current = ExecutionContext.getNextBlockInScope()
                }
            } finally {
                ExecutionContext.exitCurrentScope()
                Logger.markFinished()
            }
        }
    }
}