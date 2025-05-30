package com.unewexp.superblockly.blocks.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.CompareType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class CompareNumbers(
    id: UUID = UUID.randomUUID()
) : Block(id, BlockType.COMPARE_NUMBERS_BLOCK) {

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

    var compareType by mutableStateOf(CompareType.EQUAL)

    override suspend fun evaluate(): Boolean {
        checkDebugPause()

        val leftValue = leftInputConnector.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Левое выражение отсутствует или не Int")
        val rightValue = rightInputConnector.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Правое выражение отсутствует или не Int")

        return compareElements(leftValue, rightValue)
    }

    fun compareElements(leftValue: Int, rightValue: Int): Boolean {
        return when (compareType) {
            CompareType.EQUAL -> leftValue == rightValue
            CompareType.GREATER_EQUAL -> leftValue >= rightValue
            CompareType.LESS_EQUAL -> leftValue <= rightValue
            CompareType.NOT_EQUAL -> leftValue != rightValue
            CompareType.GREATER -> leftValue > rightValue
            CompareType.LESS -> leftValue < rightValue
        }
    }

}