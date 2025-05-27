package com.unewexp.superblockly.blocks.arithmetic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.OperandType
import java.util.UUID

class ShorthandArithmeticOperatorBlock(var initialVariableName: String = "Undefined")
    : VoidBlock(UUID.randomUUID(), BlockType.SHORTHAND_ARITHMETIC_BLOCK) {

    var selectedVariableName by mutableStateOf(initialVariableName)

    var operand by mutableStateOf(OperandType.PLUS)

    val inputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )


    override fun execute() {
        if (!ExecutionContext.hasVariable(selectedVariableName)){
            throw IllegalStateException("Переменная $selectedVariableName не существует")
        }

        val currentValue = ExecutionContext.getVariable(selectedVariableName) as? Int
            ?: throw IllegalStateException("Значение переменной не является числом Int")

        val additionalValue = inputConnector.connectedTo?.evaluate() as? Int
            ?: throw java.lang.IllegalStateException("Значение справа не является числом Int")

        ExecutionContext.changeVariableValue(selectedVariableName, performOperation(currentValue, additionalValue))
    }

    private fun performOperation(left: Int, right: Int): Int {
        return when (operand) {
            OperandType.PLUS -> left + right
            OperandType.MINUS -> left - right
            OperandType.MULTIPLICATION -> left * right
            OperandType.DIVISION -> if (right != 0) left / right else 0
            OperandType.MODULO -> if (right != 0) left % right else 0
        }
    }
}