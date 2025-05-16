package com.example.myfirstapplicatioin.utils

import com.example.myfirstapplicatioin.model.Connector

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