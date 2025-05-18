package com.unewexp.superblockly.blocks.logic


import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ErrorHandler
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

// либо сделать глобальный счетчик текущей позиции, либо нужно париться с переписыванием условий


class IfBlock : ConditionBlock(UUID.randomUUID(), BlockType.IF_BLOCK) {
    override fun execute() {
        val condition = conditionConnector.connectedTo?.evaluate() as? Boolean
            ?: throw IllegalStateException("Выражение не возвращает Boolean")

        executeConditionBlock(condition)

    }
}