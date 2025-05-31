package com.unewexp.superblockly.blocks.loops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID
import kotlin.math.abs

class ForBlock(var initialName: String = "i") : LoopBlock(UUID.randomUUID(), BlockType.FOR_BLOCK) {

    var variableName by mutableStateOf(initialName)

    val initialValueBlock = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val maxValueBlock = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val stepBlock = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    override suspend fun execute() {
        checkDebugPause()
        val currentValueVariable = initialValueBlock.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Начальное значение переменной должно быть типа Int")

        val lastValue = maxValueBlock.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Последнее значение в цикле должно быть Int")

        val stepValue = abs(stepBlock.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Шаг в цикле должен быть Int"))

        if (lastValue >= currentValueVariable){
            for (i in currentValueVariable..lastValue step stepValue){
                (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                    ExecutionContext.enterNewScope(firstBlock)
                    ExecutionContext.declareVariable(variableName, i)
                    executeInnerBlocks(firstBlock)
                }
            }
        } else {
            for (i in currentValueVariable downTo lastValue step stepValue){
                checkDebugPause()
                (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                    ExecutionContext.enterNewScope(firstBlock)
                    ExecutionContext.declareVariable(variableName, i)
                    executeInnerBlocks(firstBlock)
                }
            }
        }
    }

}