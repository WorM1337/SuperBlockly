package com.unewexp.superblockly.viewBlocks

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.getColorByBlockType
import com.unewexp.superblockly.ui.theme.BooleanColor
import com.unewexp.superblockly.ui.theme.MathColor
import com.unewexp.superblockly.ui.theme.PrintColor
import com.unewexp.superblockly.ui.theme.StartColor
import com.unewexp.superblockly.ui.theme.VariablesColor
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

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .size(currentBlock.width.value, currentBlock.height.value)
            .background(color, shape = MaterialTheme.shapes.small)
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
