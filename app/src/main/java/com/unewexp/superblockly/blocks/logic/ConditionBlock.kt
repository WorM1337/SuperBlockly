package com.unewexp.superblockly.blocks.logic

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ErrorHandler
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

open class ConditionBlock(
    idBlock: UUID,
    blockType: BlockType
) : VoidBlock(idBlock, blockType) {

    val otherConditions = setOf(
        BlockType.ELSE_BLOCK,
        BlockType.IF_ELSE_BLOCK
    )

    val conditionConnector = Connector( // условие выполнения блока
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.COMPARE_NUMBERS_BLOCK,
            BlockType.NOT_BLOCK,
            BlockType.BOOLEAN_LOGIC_BLOCK,
            BlockType.BOOLEAN_LITERAL
        ),
        allowedDataTypes = setOf(Boolean::class.java)
    )

    val innerConnector = Connector( // соединение для внутреннего блока в условии
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
        )
    )

    override val bottomConnector = Connector( // переход на следующий блок условия или дальше в программу
        connectionType = ConnectorType.STRING_BOTTOM_OUTER,
        sourceBlock = this,
        allowedBlockTypes = setOf(
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
        )
    )


    protected suspend fun executeConditionBlock(condition: Boolean){
        if (condition){
            executeInnerBlocks()
            skipRemainingConditionBlocks()
        } else {
            skipToNextConditionOrExecute()
        }
    }

    protected suspend fun executeInnerBlocks(){
        (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
            ExecutionContext.enterNewScope(firstBlock)

            try {
                var current: Block? = firstBlock
                while (current != null){
                    try{
                        current.execute()
                    } catch (ex: Exception){
                        ErrorHandler.setBlockError(current, ex.message ?: "Неизвестная ошибка")
                    }
                    current = ExecutionContext.getNextBlockInScope()
                }
            } finally{
                ExecutionContext.exitCurrentScope()
            }
        }
    }

    protected fun skipRemainingConditionBlocks(){
        var next = ExecutionContext.checkNextBlockInScope()

        if (next != null && otherConditions.contains(next.blockType)){
            next = ExecutionContext.getNextBlockInScope()
        }
    }

    protected suspend fun skipToNextConditionOrExecute(){
        val next = ExecutionContext.checkNextBlockInScope()
        if (next != null && otherConditions.contains(next.blockType)){
            ExecutionContext.getNextBlockInScope()?.execute()
        }
    }


}