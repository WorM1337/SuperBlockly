package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class PrintBlock(
    id: UUID = UUID.randomUUID(),
    blockType: BlockType = BlockType.PRINT_BLOCK
) : VoidBlock(id, blockType) {

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.STRING_CONCAT,
            BlockType.INT_LITERAL,
            BlockType.OPERAND,
            BlockType.BOOLEAN_LITERAL,
            BlockType.STRING_LITERAL,
        )
    )

    override fun execute() {
        val value = inputConnector.connectedTo?.evaluate()
            ?: throw IllegalStateException("В PrintBlock не добавлено значение вывода")
        ExecutionContext.appendLog(value.toString())
    }

}