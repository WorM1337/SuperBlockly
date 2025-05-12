package com.example.myfirstapplicatioin.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.example.myfirstapplicatioin.blocks.Block

enum class ConnectorType {
    INPUT,
    OUTPUT,
    STRING_CONNECTOR_INPUT,
    STRING_CONNECTOR_OUTPUT,
}

enum class ValueType{
    INT,
    STRING,
    BOOLEAN,
    ALL,
    EXPRESSION
}

enum class BlockType{
    INT,
    STRING,
    VARIABLE,
    EXPRESSION,
    NONE,
}

enum class VariableSupports{
    INT,
    STRING,
    BOOLEAN,
    EXPRESSION,
}

// эта штука нужна для отрисовки соединения
// в дальнейшем для каждого типа input или output нужно будет создать отдельную отрисовку, чтобы красиво отрисовывать)
data class connectionView(
    val connector: Connector,
    var positionX: Dp, // это позиция отностительно блока, поэтому нужно будет провести перерасчет
    var positionY: Dp
)


data class Connector(
    val connectionType: ConnectorType,
    val sourceBlock: Block,
    var connectedTo: Block? = null
)