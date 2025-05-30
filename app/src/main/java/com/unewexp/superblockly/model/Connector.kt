package com.example.myfirstapplicatioin.model

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block

import com.unewexp.superblockly.blocks.list.GetValueByIndex
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.ExtendConnectionViewType


data class ConnectionView(
    val connector: Connector,
    var positionX: Dp,
    var positionY: Dp,
    val extendType: ExtendConnectionViewType = ExtendConnectionViewType.NONE,
    var isConnected: Boolean = false,
    var width: Dp = 0.dp,
    var height: Dp = 0.dp,
)

data class Connector(
    val connectionType: ConnectorType,
    val sourceBlock: Block,
    var connectedTo: Block? = null,
    val allowedBlockTypes: Set<BlockType> = emptySet(),
    val allowedDataTypes: Set<Class<*>> = emptySet(),


    ) {
    fun canConnect(target: Connector): Boolean {


        if (connectionType == target.connectionType) return false
        if (connectedTo != null || target.connectedTo != null) return false



        if (allowedBlockTypes.isNotEmpty() &&
            !allowedBlockTypes.contains(target.sourceBlock.blockType)
        ) {
            return false
        }


        if (allowedDataTypes.isNotEmpty()) {
            val targetDataType = getDataType(target.sourceBlock)
            if (targetDataType != null && !allowedDataTypes.contains(targetDataType)) {
                return false
            }
        }


        return true
    }

    private fun getDataType(block: Block): Class<*>? {
        return when (block.blockType) {
            BlockType.INT_LITERAL -> Int::class.java
            BlockType.STRING_LITERAL -> String::class.java
            BlockType.BOOLEAN_LITERAL -> Boolean::class.java
            BlockType.BOOLEAN_LOGIC_BLOCK -> Boolean::class.java
            BlockType.COMPARE_NUMBERS_BLOCK -> Boolean::class.java
            BlockType.STRING_CONCAT -> String::class.java
            BlockType.STRING_APPEND -> String::class.java
            BlockType.VARIABLE_REFERENCE -> {
                if (ExecutionContext.hasVariable((block as VariableReferenceBlock).selectedVariableName)) {
                    ExecutionContext.getVariableType(block.selectedVariableName)
                } else {
                    null
                }
            }

            BlockType.NOT_BLOCK -> Boolean::class.java
            BlockType.GET_LIST_SIZE -> Int::class.java
            BlockType.GET_VALUE_BY_INDEX -> {
                if ((block as GetValueByIndex).listConnector.connectedTo != null &&
                    ExecutionContext.hasVariable((block.listConnector.connectedTo as VariableReferenceBlock).selectedVariableName)
                ) {
                    ExecutionContext.getVariableType((block.listConnector.connectedTo as VariableReferenceBlock).selectedVariableName)?.javaClass
                } else {
                    null
                }
            }

            BlockType.OPERAND -> Int::class.java
            BlockType.FIXED_VALUE_AND_SIZE_LIST -> MutableList::class.java
            else -> null
        }
    }
}