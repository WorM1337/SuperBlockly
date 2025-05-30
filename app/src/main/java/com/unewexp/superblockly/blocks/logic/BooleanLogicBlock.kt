package com.unewexp.superblockly.blocks.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.BooleanLogicType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class BooleanLogicBlock : Block(UUID.randomUUID(), BlockType.BOOLEAN_LOGIC_BLOCK){

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,

    )

    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Boolean::class.java)
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Boolean::class.java)
    )

    var logicOperand by mutableStateOf(BooleanLogicType.AND)

    override suspend fun evaluate(): Boolean {
        checkDebugPause()

        val leftValue = leftInputConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Левое соединение отсутствует или не Boolean")

        val rightValue = rightInputConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Правое соединение отсутствует или не Boolean")

        return compareElements(leftValue, rightValue)
    }

    fun compareElements(left: Boolean, right: Boolean) : Boolean{
        return when(logicOperand) {
            BooleanLogicType.AND -> left && right
            BooleanLogicType.OR -> left || right
        }
    }
}