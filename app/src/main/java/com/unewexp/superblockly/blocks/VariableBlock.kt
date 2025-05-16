package com.example.myfirstapplicatioin.blocks

import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.model.BlockType
import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.model.ConnectorType
import com.example.myfirstapplicatioin.model.ValueType
import com.example.myfirstapplicatioin.model.VariableSupports

import java.util.UUID

class VariableBlock(var name: String): Block(UUID.randomUUID(), BlockType.VARIABLE) {

    val valueConnector = Connector(ConnectorType.INPUT, this) // для соединения со значением.

    override fun execution(context: ExecutionContext) {
        context.declareVariable(name)
    }
}