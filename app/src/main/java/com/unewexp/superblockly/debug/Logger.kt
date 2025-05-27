package com.unewexp.superblockly.debug

import androidx.compose.runtime.mutableStateListOf

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