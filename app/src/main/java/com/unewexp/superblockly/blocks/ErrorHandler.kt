package com.unewexp.superblockly.blocks

import androidx.compose.runtime.mutableStateMapOf
import java.util.UUID

object ErrorHandler {
    private val blockErrors = mutableStateMapOf<UUID, String>()

    fun setError(blockId: UUID, message: String){
        blockErrors[blockId] = message
    }

    fun clearError(blockId: UUID){
        blockErrors.remove(blockId)
    }

    fun getError(blockId: UUID) : String? = blockErrors[blockId]

    fun hasError(blockId: UUID) : Boolean = blockId in blockErrors

    fun clearAllErrors(){
        blockErrors.clear()
    }

    fun getAllErrors(): Map<UUID, String> = blockErrors
}