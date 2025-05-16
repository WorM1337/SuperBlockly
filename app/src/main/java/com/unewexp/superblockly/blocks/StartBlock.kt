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
        allowedBlockTypes = setOf(BlockType.VOID_BLOCK)
    )

    override fun execute() {
        ErrorHandler.clearAllErrors()

        var nextBlock = nextBlockConnector.connectedTo
        while (nextBlock != null){
            try{
                nextBlock.execute()
            } catch (e: Exception){
                ErrorHandler.setError(nextBlock.id, e.message ?: "Неизвестная ошибка")
                break
            }

            nextBlock = (nextBlock as? VoidBlock)?.getNextBlock()
                ?: throw IllegalStateException("Не корректное соединение")
        }
    }
}