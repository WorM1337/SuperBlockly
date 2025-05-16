package com.example.myfirstapplicatioin.viewBlocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.example.myfirstapplicatioin.model.ConnectionView

abstract class ViewBlock(
    var initialX: Dp,
    var initialY: Dp
) {

    abstract var listConnectors: List<ConnectionView>

    @Composable
    protected abstract fun _render(content: @Composable () -> Unit, width: Dp, height: Dp)
    @Composable
    abstract fun render()
    @Composable
    abstract fun View()
}