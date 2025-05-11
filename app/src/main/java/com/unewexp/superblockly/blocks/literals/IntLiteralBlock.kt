package com.example.myfirstapplicatioin.blocks.literals

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.ExecutionContext
import com.example.myfirstapplicatioin.model.BlockType
import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.model.ConnectorType
import com.example.myfirstapplicatioin.model.ValueType
import java.util.UUID

class IntLiteralBlock(
    val value : Int
) : Block(UUID.randomUUID(), BlockType.INT) {

    val outputConnector = Connector(ConnectorType.OUTPUT, this)

    override fun execution(context: ExecutionContext) {

    }

}