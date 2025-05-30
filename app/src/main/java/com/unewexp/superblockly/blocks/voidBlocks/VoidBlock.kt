package com.unewexp.superblockly.blocks.voidBlocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID


open class VoidBlock(
    id: UUID,
    blockType: BlockType
) : Block(id, blockType) {

    open val topConnector = Connector(
        connectionType = ConnectorType.STRING_TOP,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.START,
            BlockType.STRING_APPEND,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.VARIABLE_DECLARATION,
            BlockType.PRINT_BLOCK,
            BlockType.IF_BLOCK,
            BlockType.ELSE_BLOCK,
            BlockType.IF_ELSE_BLOCK,
            BlockType.REPEAT_N_TIMES,
            BlockType.WHILE_BLOCK,
            BlockType.SHORTHAND_ARITHMETIC_BLOCK,
            BlockType.FOR_BLOCK,
            BlockType.FOR_ELEMENT_IN_LIST,
            BlockType.ADD_VALUE_BY_INDEX,
            BlockType.REMOVE_VALUE_BY_INDEX,
            BlockType.PUSH_BACK_ELEMENT,
            BlockType.EDIT_VALUE_BY_INDEX
        )
    )


    open val bottomConnector = Connector(
        connectionType = ConnectorType.STRING_BOTTOM_OUTER,

        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.IF_BLOCK,
            BlockType.STRING_APPEND,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.PRINT_BLOCK,
            BlockType.VARIABLE_DECLARATION,
            BlockType.REPEAT_N_TIMES,
            BlockType.WHILE_BLOCK,
            BlockType.SHORTHAND_ARITHMETIC_BLOCK,
            BlockType.FOR_BLOCK,
            BlockType.FOR_ELEMENT_IN_LIST,
            BlockType.ADD_VALUE_BY_INDEX,
            BlockType.REMOVE_VALUE_BY_INDEX,
            BlockType.PUSH_BACK_ELEMENT,
            BlockType.EDIT_VALUE_BY_INDEX
        )
    )

    fun getNextBlock(): Block?{
        return bottomConnector.connectedTo
    }



}