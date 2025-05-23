package com.unewexp.superblockly.blocks.arithmetic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.OperandType
import java.util.UUID

class OperandBlock : Block(UUID.randomUUID(), BlockType.OPERAND) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )

    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    var operand by mutableStateOf(OperandType.PLUS)

    override fun evaluate(): Int {
        val left = leftInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalArgumentException("Ожидается числовое значение")
        val right = rightInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalArgumentException("Ожидается числовое значение")
        return performOperation(left, right)
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