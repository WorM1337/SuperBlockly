package com.unewexp.superblockly.blocks.voidBlocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class VariableDeclarationBlock(var initialName: String = "Undefined") : VoidBlock(UUID.randomUUID(), BlockType.VARIABLE_DECLARATION) {
    val valueInputConnector = Connector(
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

    var name by mutableStateOf(initialName)


    private var variableType: Class<*>? = null

    override fun execute() {
        validateName()
        val value = valueInputConnector.connectedTo?.evaluate()
            ?: throw IllegalStateException("Не указано значение для переменной '$name'")

        checkTypeConsistency(value)
        ExecutionContext.setVariable(name, value)
        variableType = value.javaClass
    }



    private fun validateName() {
        if (name.isBlank()) throw IllegalStateException("Имя переменной не может быть пустым")
        if (ExecutionContext.hasVariable(name) || name.equals("Undefined")) {
            throw IllegalStateException("Имя переменной существует или Undefined")
        }
    }

    private fun checkTypeConsistency(value: Any) {
        if (variableType != null && variableType != value.javaClass) {
            throw IllegalStateException("Нельзя изменить тип переменной '$name'")
        }
    }

    fun getVariableType(): Class<*>? = variableType
}