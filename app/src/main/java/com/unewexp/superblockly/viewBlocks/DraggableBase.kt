package com.unewexp.superblockly.viewBlocks

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.getColorByBlockType
import kotlin.math.roundToInt


@Composable
fun DraggableBase(
    content: @Composable () -> Unit,
    draggableBlock: DraggableBlock,
    onPositionChanged: (Float, Float) -> Unit,
    onDoubleTap: () -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit
){

    val currentBlock by rememberUpdatedState(draggableBlock)
    val latestOnPositionChanged by rememberUpdatedState(onPositionChanged)
    val latestOnDoubleTap by rememberUpdatedState(onDoubleTap)
    val latestOnDragStart by rememberUpdatedState(onDragStart)
    val latestOnDragEnd by rememberUpdatedState(onDragEnd)
    var offsetX = currentBlock.x.value
    var offsetY = currentBlock.y.value

    val color = getColorByBlockType(currentBlock.block.blockType)

    val border = if (draggableBlock.block.hasException) {
        Log.i(currentBlock.block.blockType.toString(), "ошибка на блоке")
        BorderStroke(3.dp, Color.Red)
    } else if (draggableBlock.block.isDebug){
        BorderStroke(3.dp, Color.Green)
    } else {
        BorderStroke(0.dp, Color.Red.copy(alpha = 0f))
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .wrapContentSize(unbounded = true, align = Alignment.TopCenter)
            .requiredSize(currentBlock.width.value, currentBlock.height.value)
            .background(color, shape = MaterialTheme.shapes.small)
            .border(border, MaterialTheme.shapes.small)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        latestOnDragStart()
                    },
                    onDragEnd = {
                        latestOnDragEnd()
                    }
                ) { change, dragAmount ->
                    change.consume()
                    latestOnPositionChanged(dragAmount.x, dragAmount.y)
                }
            }
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
                        latestOnDoubleTap()
                    }
                )
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .zIndex(currentBlock.zIndex.value)
    ) {
        content()
    }
}
