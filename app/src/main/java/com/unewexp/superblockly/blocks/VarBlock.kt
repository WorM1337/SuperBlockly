package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.ExecutionContext

class VarBlock(name: String?) : ReturnBlock() {
    override fun getInformationForTree(): MutableList<Block>? {
        return null
    }
    override fun execution(context: ExecutionContext) {}
}