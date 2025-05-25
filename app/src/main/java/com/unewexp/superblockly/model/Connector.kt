package com.example.myfirstapplicatioin.model

import androidx.compose.ui.unit.Dp
import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType


data class ConnectionView(
    val connector: Connector,
    var positionX: Dp,
    var positionY: Dp,
    var isConnected: Boolean = false
)

data class Connector(
    val connectionType: ConnectorType,
    val sourceBlock: Block,
    var connectedTo: Block? = null,
    val allowedBlockTypes: Set<BlockType> = emptySet(),
    val allowedDataTypes: Set<Class<*>> = emptySet(),

    // Class<*> объект Java-класса неизвестного типа
) {
    fun canConnect(target: Connector): Boolean {

        // Проверяем базовые условия соединения
        if (connectionType == target.connectionType) return false
        if (connectedTo != null || target.connectedTo != null) return false


        // Проверяем допустимые типы блоков
        if (allowedBlockTypes.isNotEmpty() &&
            !allowedBlockTypes.contains(target.sourceBlock.blockType)) {
            return false
        }

        // Проверяем совместимость типов данных
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

            else -> null
        }
    }
}