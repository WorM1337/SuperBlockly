package com.unewexp.superblockly.blocks.returnBlocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.OperandType
import java.util.UUID


class OperandBlock : Block(UUID.randomUUID(), BlockType.OPERAND) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.INT_LITERAL,
            BlockType.VARIABLE_REFERENCE
        ),
        allowedDataTypes = setOf(Int::class.java)
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.INT_LITERAL,
            BlockType.VARIABLE_REFERENCE
        ),
        allowedDataTypes = setOf(Int::class.java)
    )

    var operand = OperandType.PLUS


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
        }
    }
}