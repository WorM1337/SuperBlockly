package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class PrintBlock() : VoidBlock(UUID.randomUUID(), BlockType.PRINT_BLOCK) {

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java, String::class.java, Boolean::class.java)
    )

    override fun execute() {
        val value = inputConnector.connectedTo?.evaluate()
            ?: throw IllegalStateException("В PrintBlock не добавлено значение вывода")
        ExecutionContext.appendLog(value.toString())
    }

}