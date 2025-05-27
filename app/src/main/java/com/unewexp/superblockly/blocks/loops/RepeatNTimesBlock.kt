package com.unewexp.superblockly.blocks.loops

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class RepeatNTimesBlock() : LoopBlock(UUID.randomUUID(), BlockType.REPEAT_N_TIMES) {

    val countRepeatTimesConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java),
    )

    override fun execute() {
        val countTimes = countRepeatTimesConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Передаваемое значение не является Int")
        for (i in 1..countTimes){
            (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                executeInnerBlocks(firstBlock)
            }
        }
    }
}