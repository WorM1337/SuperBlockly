package com.example.myfirstapplicatioin.utils

import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ErrorHandler
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


fun safeConnect(source: Connector, target: Connector){ // для сани, пользуемся этой штукой для соединения блоков
        try{
                connectTo(source, target)
        } catch (ex: Exception){
                ErrorHandler.setConnectionError(source.sourceBlock.id, ex.message ?: "Ошибка соединения блоков")
                ErrorHandler.setConnectionError(target.sourceBlock.id, ex.message ?: "Ошибка соединения блоков")

                source.sourceBlock.hasException = true
                target.sourceBlock.hasException = true

        }
}


fun checkTypeConnector(source: Connector, target: Connector): Boolean{
        return when (source.connectionType) {
                ConnectorType.INPUT -> target.connectionType == ConnectorType.OUTPUT
                ConnectorType.OUTPUT -> target.connectionType == ConnectorType.INPUT
                ConnectorType.STRING_TOP -> target.connectionType == ConnectorType.STRING_BOTTOM_OUTER || target.connectionType == ConnectorType.STRING_BOTTOM_INNER
                ConnectorType.STRING_BOTTOM_OUTER, ConnectorType.STRING_BOTTOM_INNER -> target.connectionType == ConnectorType.STRING_TOP
        }
}

fun connectTo(source: Connector, target: Connector) {

        require(checkTypeConnector(source, target)){
                "Блоки не могут быть соединены. Проверьте типы соединений"
        }


        require(source.canConnect(target)) {
                "Эти блоки не могут быть соединены. " +
                        "Проверьте типы блоков и допустимые типы данных."
        }

        source.connectedTo = target.sourceBlock
        target.connectedTo = source.sourceBlock

}

fun disconnect(source: Connector, target: Connector) {
        source.connectedTo = null
        target.connectedTo = null

        source.sourceBlock.hasException = false
        target.sourceBlock.hasException = false

        ErrorHandler.clearConnectionError(source.sourceBlock.id)
        ErrorHandler.clearConnectionError(target.sourceBlock.id)
}

fun canConnect(source: Connector, target: Connector): Boolean {
        return source.connectionType != target.connectionType &&
                source.canConnect(target)
}

fun isConnected(connector: Connector): Boolean{
        return connector.connectedTo != null
}

fun connectorDistance(connectionView1: ConnectionView, connectionView2: ConnectionView): Float {
        return sqrt((connectionView1.positionX.value - connectionView2.positionX.value).pow(2) + (connectionView1.positionY.value - connectionView2.positionY.value).pow(2))
}