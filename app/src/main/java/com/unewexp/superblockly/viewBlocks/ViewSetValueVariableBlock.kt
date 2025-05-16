package com.unewexp.superblockly.viewBlocks

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock

class ViewSetValueVariableBlock(initialX: Dp, initialY: Dp) : InlineViewBlock(initialX, initialY) {
    var block = SetValueVariableBlock()
    override var listConnectors: List<ConnectionView> = listOf(ConnectionView(block.valueConnector, initialX, initialY))

    @Composable
    override fun render() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun View() {
        TODO("Not yet implemented")
    }

}