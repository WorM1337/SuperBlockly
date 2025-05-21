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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.model.ConnectorManager
import kotlin.math.roundToInt

@Composable
fun DraggableBase(
    content: @Composable () -> Unit,
    draggableBlock: DraggableBlock,
    onPositionChanged: (Float, Float) -> Unit,
    onDoubleTap: () -> Unit,
    onDragEnd: () -> Unit
){


    var offsetX by remember { draggableBlock.x }
    var offsetY by remember { draggableBlock.y }


    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(draggableBlock.width.dp, draggableBlock.height.dp)
            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        onDragEnd()
                    }
                ) { change, dragAmount ->
                    change.consume()
                    Log.i("IdBlock", draggableBlock.id)
                    onPositionChanged(dragAmount.x, dragAmount.y)
                }
            }
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
                        onDoubleTap()
                    }
                )
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        content()
    }
}