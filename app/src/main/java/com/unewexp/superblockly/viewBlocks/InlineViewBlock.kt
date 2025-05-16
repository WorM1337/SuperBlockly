package com.unewexp.superblockly.viewBlocks

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.viewBlocks.ViewBlock
import kotlin.math.roundToInt

abstract class InlineViewBlock(initialX: Dp, initialY: Dp) : ViewBlock( initialX, initialY) {
    @Composable
    override fun _render( content: @Composable () -> Unit, width: Dp, height: Dp){
        val density = LocalDensity.current

        val initialPxX = with(density) { initialX.toPx() }
        val initialPxY = with(density) { initialY.toPx() }

        var offsetX by remember { mutableStateOf(initialPxX) }
        var offsetY by remember { mutableStateOf(initialPxY) }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(width, 60.dp)
                .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            content()
        }
    }
}