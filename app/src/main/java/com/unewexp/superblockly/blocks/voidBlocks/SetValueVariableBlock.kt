package com.unewexp.superblockly.blocks.voidBlocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException

import com.unewexp.superblockly.debug.ExecutionContext
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
            BlockType.VARIABLE_REFERENCE,
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.GET_VALUE_BY_INDEX,
            BlockType.FIXED_VALUE_AND_SIZE_LIST,
            BlockType.GET_LIST_SIZE,
        )
    )

    override suspend fun execute() {
        checkDebugPause()


        if (selectedVariableName.isBlank()) {
            throw BlockIllegalStateException(this, "Не указано имя переменной")
        }

        val value = valueConnector.connectedTo?.evaluate()
            ?: throw BlockIllegalStateException(this,"Не указано значение для переменной '$selectedVariableName'")

        if (!ExecutionContext.hasVariable(selectedVariableName)){
            throw BlockIllegalStateException(this, "Указанная переменная '$selectedVariableName' не объявлена")
        }

        val currentValue = ExecutionContext.getVariable(selectedVariableName)
        if (currentValue != null && currentValue.javaClass != value.javaClass) {
            throw BlockIllegalStateException(this, "Нельзя изменить тип переменной '$selectedVariableName'б: ${currentValue.javaClass}")
        }

        ExecutionContext.changeVariableValue(selectedVariableName, value)
    }

}