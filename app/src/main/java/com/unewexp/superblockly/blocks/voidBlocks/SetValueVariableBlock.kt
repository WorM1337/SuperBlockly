package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.model.Connector

import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class SetValueVariableBlock : VoidBlock(UUID.randomUUID(), BlockType.SET_VARIABLE_VALUE) {
    var variableName: String = ""
        private set

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
        if (variableName.isBlank()) {
            throw IllegalStateException("Не указано имя переменной")
        }

        val value = valueConnector.connectedTo?.evaluate()
            ?: throw IllegalStateException("Не указано значение для переменной '$variableName'")

        val currentValue = ExecutionContext.getVariable(variableName)
        if (currentValue != null && currentValue.javaClass != value.javaClass) {
            throw IllegalStateException("Нельзя изменить тип переменной '$variableName'")
        }

        ExecutionContext.setVariable(variableName, value)
    }

    fun setNameVariable(name: String) {
        if (name.isBlank()) {
            throw IllegalArgumentException("Имя переменной не может быть пустым")
        }
        this.variableName = name
    }
}