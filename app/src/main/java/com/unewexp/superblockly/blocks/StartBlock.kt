package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class StartBlock : VoidBlock(UUID.randomUUID(), BlockType.START) {
    val nextBlockConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.PRINT_BLOCK,
            BlockType.IF_BLOCK,
            BlockType.VARIABLE_DECLARATION,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.STRING_APPEND,
        )
    )

    override fun execute() {
        ErrorHandler.clearAllErrors()
        ExecutionContext.clearLogs()
        ExecutionContext.clearVariables()

        getNextBlock()?.let { firstBlock ->
            ExecutionContext.enterNewScope(firstBlock)
            try {
                var current: Block? = firstBlock
                while (current != null) {
                    try {
                        current.execute()
                    } catch (e: Exception) {
                        ErrorHandler.setError(current.id, e.message ?: "Неизвестная ошибка")
                        break
                    }
                    current = ExecutionContext.getNextBlockInScope()
                }
            } finally {
                ExecutionContext.exitCurrentScope()
            }
        }
    }
}