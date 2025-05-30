package com.unewexp.superblockly.blocks.voidBlocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class StringAppendBlock : VoidBlock(UUID.randomUUID(), BlockType.STRING_APPEND) {

    var variableName by mutableStateOf("Undefined")

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.STRING_LITERAL,
            BlockType.VARIABLE_REFERENCE,
            BlockType.STRING_CONCAT,
            BlockType.INT_LITERAL,
            BlockType.NOT_BLOCK,
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.BOOLEAN_LITERAL,
            BlockType.GET_VALUE_BY_INDEX,
            BlockType.GET_LIST_SIZE,
            BlockType.OPERAND
        ),
        allowedDataTypes = setOf(
            String::class.java,
            Int::class.java,
            Boolean::class.java
        )
    )

    override suspend fun execute() {
        checkDebugPause()

        if (!ExecutionContext.hasVariable(variableName)) {
            throw IllegalStateException("Переменная $variableName не существует или неверное название")
        }
        var currentValue = ExecutionContext.getVariable(variableName) as? String
            ?: throw IllegalStateException("Переменная $variableName не является строкой")

        var stringToAppend = inputConnector.connectedTo?.evaluate()

        ExecutionContext.changeVariableValue(variableName, currentValue + stringToAppend)
    }
}