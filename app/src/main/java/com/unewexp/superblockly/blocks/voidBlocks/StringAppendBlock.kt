package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class StringAppendBlock : VoidBlock(UUID.randomUUID(), BlockType.STRING_APPEND) {
    val variableInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(BlockType.VARIABLE_REFERENCE),
        allowedDataTypes = setOf(String::class.java)
    )

    val stringToAppendConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.STRING_LITERAL,
            BlockType.VARIABLE_REFERENCE,
            BlockType.STRING_CONCAT,
            BlockType.INT_LITERAL
        ),
        allowedDataTypes = setOf(String::class.java, Int::class.java)
    )

    override fun execute() {
        val variableName = (variableInputConnector.connectedTo as? VariableReferenceBlock)?.selectedVariableName
            ?: throw IllegalStateException("Не указана переменная")

        val currentValue = ExecutionContext.getVariable(variableName) as? String
            ?: throw IllegalStateException("Переменная '$variableName' не является строкой")

        val stringToAppend = stringToAppendConnector.connectedTo?.evaluate()?.toString()
            ?: throw IllegalStateException("Не указана строка для добавления")

        ExecutionContext.setVariable(variableName, currentValue + stringToAppend)
    }
}