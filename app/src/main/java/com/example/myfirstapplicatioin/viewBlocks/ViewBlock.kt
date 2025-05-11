package com.example.myfirstapplicatioin.viewBlocks

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.connectionView

open class ViewBlock(
    val block: Block,
    var initialX: Dp,
    var initialY: Dp
) {

    open val inputConnectors: List<connectionView> = emptyList()
    open val outputConnectors: List<connectionView> = emptyList()




}