package com.unewexp.superblockly.blocks

import androidx.compose.runtime.mutableStateMapOf
import com.unewexp.superblockly.blocks.Logger.LogType
import java.util.UUID


object ErrorHandler {
    private val blockErrors = mutableStateMapOf<UUID, String>()
    private val connectionErrors = mutableStateMapOf<UUID, String>()

    fun setBlockError(blockId: UUID, message: String){
        blockErrors[blockId] = message
        Logger.appendLog(LogType.ERROR, message)
    }

    fun setConnectionError(blockId: UUID, message: String){
        connectionErrors[blockId] = message
    }

    fun clearConnectionError(blockId: UUID){
        connectionErrors.remove(blockId)
    }

    fun clearBlockErrors(){
        blockErrors.clear()
    }

    fun getAllConnectionErrors(): Map<UUID, String> = connectionErrors.toMap()
    fun getAllBlockErrors(): Map<UUID, String> = blockErrors.toMap()
}