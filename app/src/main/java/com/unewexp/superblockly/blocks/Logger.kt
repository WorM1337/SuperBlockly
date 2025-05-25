package com.unewexp.superblockly.blocks

import androidx.compose.runtime.mutableStateListOf
import kotlin.text.clear

object Logger {
    private val _logs = mutableStateListOf<Log>();
    val logs: List<Log> get() = _logs;


    fun appendLog(type: LogType, message: String){
        _logs.add(Log(type, message))
    }

    fun clearLogs(){
        _logs.clear()
    }

    enum class LogType{
        ERROR,
        TEXT,
    }

    data class Log(
        val logType: LogType,
        val message: String,
    )
}