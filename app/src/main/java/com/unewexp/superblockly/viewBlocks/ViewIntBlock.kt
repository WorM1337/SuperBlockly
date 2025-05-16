package com.example.myfirstapplicatioin.viewBlocks

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.connectionView
import kotlin.math.roundToInt

class ViewIntBlock(
    val literalBlock: IntLiteralBlock,
    initialX: Dp,
    initialY: Dp
) : ViewBlock(literalBlock, initialX, initialY) {

    override val listConnectors: List<connectionView> = listOf(
        connectionView(literalBlock.outputConnector, initialX, initialY)
    )

    @Composable
    override fun render() {
        TODO("Not yet implemented")
    }

}