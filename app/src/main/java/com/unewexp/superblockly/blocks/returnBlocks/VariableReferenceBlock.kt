package com.unewexp.superblockly.blocks.returnBlocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class VariableReferenceBlock(var name: String) : Block(UUID.randomUUID(), BlockType.VARIABLE_REFERENCE) {
    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.OPERAND,
            BlockType.STRING_CONCAT,
            BlockType.SET_VARIABLE_VALUE
        )
    )

    override fun evaluate(): Any {
        if (!ExecutionContext.hasVariable(name)) {
            throw IllegalArgumentException("Переменная '$name' не существует")
        }
        return ExecutionContext.getVariable(name)!!
    }

}