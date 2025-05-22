package com.unewexp.superblockly.viewBlocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DraggableBase(
    content: @Composable () -> Unit,
    draggableBlock: DraggableBlock,
    onPositionChanged: (Float, Float) -> Unit,
    onDoubleTap: () -> Unit,
    onDragEnd: () -> Unit
){

    val currentBlock by rememberUpdatedState(draggableBlock)
    val latestOnPositionChanged by rememberUpdatedState(onPositionChanged)
    val latestOnDoubleTap by rememberUpdatedState(onDoubleTap)
    val latestOnDragEnd by rememberUpdatedState(onDragEnd)
    var offsetX = currentBlock.x.value
    var offsetY = currentBlock.y.value

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(currentBlock.width, currentBlock.height)
            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        latestOnDragEnd()
                    }
                ) { change, dragAmount ->
                    change.consume()
                    Log.i("IdBlock", currentBlock.id)
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        content()
    }
}