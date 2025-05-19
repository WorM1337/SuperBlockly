package com.unewexp.superblockly.blocks.voidBlocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector

import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class SetValueVariableBlock : VoidBlock(UUID.randomUUID(), BlockType.SET_VARIABLE_VALUE) {

    var selectedVariableName by mutableStateOf("Undefined")

    val valueConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.INT_LITERAL,
            BlockType.STRING_LITERAL,
            BlockType.BOOLEAN_LITERAL,
            BlockType.OPERAND,
            BlockType.STRING_CONCAT,
            BlockType.VARIABLE_REFERENCE
        )
    )

    override fun execute() {
        if (selectedVariableName.isBlank()) {
            throw IllegalStateException("Не указано имя переменной")
        }

        val value = valueConnector.connectedTo?.evaluate()
            ?: throw IllegalStateException("Не указано значение для переменной '$selectedVariableName'")

        if (!ExecutionContext.hasVariable(selectedVariableName)){
            throw IllegalStateException("Указанная переменная не объявлена")
        }

        val currentValue = ExecutionContext.getVariable(selectedVariableName)
        if (currentValue != null && currentValue.javaClass != value.javaClass) {
            throw IllegalStateException("Нельзя изменить тип переменной '$selectedVariableName'")
        }

        ExecutionContext.changeVariableValue(selectedVariableName, value)
    }

}