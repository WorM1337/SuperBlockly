package com.example.myfirstapplicatioin.blocks



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.unewexp.superblockly.debug.DebugController
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.debug.RunProgram
//import com.unewexp.superblockly.blocks.BlockWithSerializableData
import com.unewexp.superblockly.enums.BlockType
import java.util.UUID


// это класс описывает функционал блока
abstract class Block(val id: UUID, val blockType: BlockType) {
    var hasException by mutableStateOf(false)
    var isDebug by mutableStateOf(false)

    open suspend fun evaluate(): Any? {
        throw UnsupportedOperationException("Этот блок не поддерживает evaluate()")
    }


    open suspend fun execute() {
        throw UnsupportedOperationException("Этот блок не поддерживает execute()")
    }


    protected suspend fun checkDebugPause(){
        if (ExecutionContext.programProgress == RunProgram.DEBUG){
            DebugController.changeDebugBlock(this)
            DebugController.checkForBreakpoint()

        }
    }
}