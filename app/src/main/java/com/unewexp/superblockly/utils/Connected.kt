package com.example.myfirstapplicatioin.utils

import com.example.myfirstapplicatioin.model.Connector


fun connectTo(source: Connector, target: Connector){
        // нужно будет здесь написать проверку, чтобы не было странной темы, что блоки ouput и input соединяются друг с другом
        source.connectedTo = target.sourceBlock
        target.connectedTo = source.sourceBlock
}

fun disconnect(source: Connector, target: Connector){
        source.connectedTo = null
        target.connectedTo = null
}
