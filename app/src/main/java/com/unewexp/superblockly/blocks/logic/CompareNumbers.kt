package com.unewexp.superblockly.blocks.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.CompareType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class CompareNumbers : Block(UUID.randomUUID(), BlockType.COMPARE_NUMBERS_BLOCK){
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
//        allowedDataTypes = setOf(Boolean::class.java)
    )
    // нужно сделать проверку типов у коннекторов, то есть тип левого коннектора равен типу правого
    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.INT_LITERAL,
            BlockType.VARIABLE_REFERENCE
        )
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.INT_LITERAL,
            BlockType.VARIABLE_REFERENCE
        )
    )

    var compareType by mutableStateOf(CompareType.GREATER)

    override fun evaluate(): Boolean {
        val leftValue = leftInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Левое выражение отсутствует или не Int")
        val rightValue = rightInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Правое выражение отсутствует или не Int")

        return compareElements(leftValue, rightValue)
    }

    fun compareElements(leftValue: Int, rightValue: Int) : Boolean{
        return when(compareType){
            CompareType.EQUAL -> leftValue == rightValue
            CompareType.GREATER_EQUAL -> leftValue >= rightValue
            CompareType.LESS_EQUAL -> leftValue <= rightValue
            CompareType.NOT_EQUAL -> leftValue != rightValue
            CompareType.GREATER -> leftValue > rightValue
            CompareType.LESS -> leftValue < rightValue
        }
    }

}