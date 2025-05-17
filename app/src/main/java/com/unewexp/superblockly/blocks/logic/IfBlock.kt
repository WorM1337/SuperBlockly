package com.unewexp.superblockly.blocks.logic


import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class IfBlock : VoidBlock(UUID.randomUUID(), BlockType.IF_BLOCK) {

    val innerConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
    )

    val conditionConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.NOT_BLOCK,
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.BOOLEAN_LITERAL
        )
    )

    override val bottomConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.IF_BLOCK,
            BlockType.STRING_APPEND,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.PRINT_BLOCK,
            BlockType.VARIABLE_DECLARATION,
            BlockType.IF_ELSE_BLOCK,
            BlockType.ELSE_BLOCK,
        )
    )


    override fun execute() {

    }
}