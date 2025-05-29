package com.unewexp.superblockly.debug

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


object Logger {
    private val _logs = mutableStateListOf<Log>();
    val logs: List<Log> get() = _logs;

    var executionFinished by mutableStateOf(false)
        private set

    fun appendLog(type: LogType, message: String){
        _logs.add(Log(type, message))
    }

    fun clearLogs(){
        _logs.clear()
        executionFinished = false
    }

    fun markFinished(){
        executionFinished = true
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