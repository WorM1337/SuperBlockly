package com.unewexp.superblockly.blocks.loops

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ErrorHandler
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

open class LoopBlock(
    id: UUID,
    blockType: BlockType
) : VoidBlock(id, blockType) {


    val innerConnector = Connector(
        connectionType = ConnectorType.STRING_BOTTOM_INNER,
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
            BlockType.EDIT_VALUE_BY_INDEX,
        )
    )

    suspend fun executeInnerBlocks(firstBlock: Block){
            try {
                var current: Block? = firstBlock
                while (current != null){
                    current.execute()
                    current = ExecutionContext.getNextBlockInScope()
                }
            } finally{
                ExecutionContext.exitCurrentScope()
            }

    }

}