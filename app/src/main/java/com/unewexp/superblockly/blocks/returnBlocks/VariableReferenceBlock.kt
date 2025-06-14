package com.unewexp.superblockly.blocks.returnBlocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class VariableReferenceBlock : Block(UUID.randomUUID(), BlockType.VARIABLE_REFERENCE) {

    var selectedVariableName by mutableStateOf("Undefined")

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )


    override suspend fun evaluate(): Any {
        checkDebugPause()

        if (!ExecutionContext.hasVariable(selectedVariableName)) {
            throw BlockIllegalStateException(this, "Переменная '$selectedVariableName' не существует")
        }
        return ExecutionContext.getVariable(selectedVariableName)!!
    }

}