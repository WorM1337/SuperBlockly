package com.unewexp.superblockly.blocks.loops

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class ForElementInListBlock : LoopBlock(UUID.randomUUID(), BlockType.FOR_ELEMENT_IN_LIST) {
    val listConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.FIXED_VALUE_AND_SIZE_LIST
        )
    )

    var elementName by mutableStateOf("k")

    override suspend fun execute() {
        checkDebugPause()
        val list = listConnector.connectedTo?.evaluate() as? List<*>
            ?: throw BlockIllegalStateException(this,"Переменная, переданная в цикл, не содержит список")

        for (item in list){
            checkDebugPause()
            (innerConnector.connectedTo as? VoidBlock)?.let{ firstBlock ->
                ExecutionContext.enterNewScope(firstBlock)
                ExecutionContext.declareVariable(elementName, item)
                executeInnerBlocks(firstBlock)
            }
        }
    }
}
