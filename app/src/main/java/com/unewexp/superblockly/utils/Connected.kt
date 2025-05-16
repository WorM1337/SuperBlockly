package com.example.myfirstapplicatioin.utils

import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.ErrorHandler
import java.lang.Math.pow
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


fun safeConnect(source: Connector, target: Connector){ // для сани, пользуемся этой штукой для соединения блоков
        try{
                connectTo(source, target)
        } catch (ex: Exception){
                ErrorHandler.setError(source.sourceBlock.id, ex.message ?: "Ошибка соединения блоков")
                ErrorHandler.setError(target.sourceBlock.id, ex.message ?: "Ошибка соединения блоков")
        }
}


fun connectTo(source: Connector, target: Connector) {

        require(source.connectionType != target.connectionType) {
                "Нельзя соединять два коннектора одного типа"
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