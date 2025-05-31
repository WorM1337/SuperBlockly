package com.unewexp.superblockly.debug

import com.example.myfirstapplicatioin.blocks.Block
import kotlinx.coroutines.channels.Channel

object DebugController {
    private var continueSignal = Channel<Unit>(Channel.RENDEZVOUS)
    private var debugBlock: Block? = null

    suspend fun checkForBreakpoint(){
        if (ExecutionContext.programProgress == RunProgram.DEBUG){
            continueSignal.receive()
        }
    }

    fun changeDebugBlock(block: Block){
        debugBlock?.isDebug = false
        block.isDebug = true
        debugBlock = block
    }


    fun continueExecution(){
        continueSignal.trySend(Unit)
    }

    fun reset(){
        continueSignal.close()
        debugBlock?.isDebug = false
        debugBlock = null
        continueSignal = Channel(Channel.RENDEZVOUS)
    }
}