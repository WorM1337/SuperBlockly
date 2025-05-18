package com.unewexp.superblockly.model

import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.enums.ConnectorType

object ConnectorManager {
    fun tryConnect(connector: ConnectionView){
        if(connector.connector.connectionType != ConnectorType.OUTPUT){
            return
        }

    }
}