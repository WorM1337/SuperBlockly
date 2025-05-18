package com.unewexp.superblockly.viewBlocks

import com.example.myfirstapplicatioin.blocks.Block

data class DraggableBlock(
    val id: String,
    val block: Block,
    var x: Float,
    var y: Float,
    var width: Int = 100,
    var height: Int = 60
)
