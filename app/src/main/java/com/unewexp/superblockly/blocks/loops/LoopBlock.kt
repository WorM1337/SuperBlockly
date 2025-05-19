package com.unewexp.superblockly.blocks.loops

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ErrorHandler
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

open class LoopBlock(
    id: UUID,
    blockType: BlockType
) : VoidBlock(id, blockType) {


    val innerConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.IF_BLOCK,
            BlockType.STRING_APPEND,
            BlockType.PRINT_BLOCK,
            BlockType.VARIABLE_DECLARATION,
            BlockType.SET_VARIABLE_VALUE,
            BlockType.REPEAT_N_TIMES,
            BlockType.WHILE_BLOCK,
            BlockType.SHORTHAND_ARITHMETIC_BLOCK,
            BlockType.FOR_BLOCK,
        )
    )

    fun executeInnerBlocks(firstBlock: Block){
            try {
                var current: Block? = firstBlock
                while (current != null){
                    try{
                        current.execute()
                    } catch (ex: Exception){
                        ErrorHandler.setError(current.id, ex.message ?: "Неизвестная ошибка")
                    }
                    current = ExecutionContext.getNextBlockInScope()
                }
            } finally{
                ExecutionContext.exitCurrentScope()
            }

    }

}