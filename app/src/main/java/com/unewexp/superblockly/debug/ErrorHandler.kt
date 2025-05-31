package com.unewexp.superblockly.debug

import androidx.compose.runtime.mutableStateMapOf
import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.debug.Logger.LogType
import java.util.UUID

class BlockIllegalStateException(
    val block: Block,
    message: String,
) : IllegalStateException(message)

object ErrorHandler {
    private val blockErrors = mutableStateMapOf<Block, String>()
    private val connectionErrors = mutableStateMapOf<UUID, String>()

    fun setBlockError(block: Block, message: String){
        blockErrors[block] = message
        block.hasException = true
        Logger.appendLog(LogType.ERROR, message)
    }

    fun setConnectionError(blockId: UUID, message: String){
        connectionErrors[blockId] = message
    }

    fun clearConnectionError(blockId: UUID){
        connectionErrors.remove(blockId)
    }

    fun clearBlockErrors(){
        for ((block, message) in blockErrors){
            block.hasException = false
        }
        blockErrors.clear()
    }

    fun getAllConnectionErrors(): Map<UUID, String> = connectionErrors.toMap()
    fun getAllBlockErrors(): Map<Block, String> = blockErrors.toMap()
}