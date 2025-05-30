package com.unewexp.superblockly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.unewexp.superblockly.ui.theme.daggerColor
import com.unewexp.superblockly.ui.theme.errorTextColor
import com.unewexp.superblockly.ui.theme.logDraggableLine
import com.unewexp.superblockly.ui.theme.logsPanelBackground
import com.unewexp.superblockly.ui.theme.logsTextColor


@Composable
fun ConsolePanel(){


    var isExpanded by remember{ mutableStateOf(true) }
    var widthDp by remember { mutableStateOf(200.dp) }

    var showLogs = Logger.executionFinished

    val minWidth = 150.dp

    val maxWidth = 650.dp
    val rolledUp = 16.dp

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(Alignment.End)
            .width( if (isExpanded) widthDp else rolledUp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    bottomStart = 20.dp,
                )
            )
    ) {

        Row(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(rolledUp)
                    .background(
                        color = logDraggableLine,
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                        )
                    )
                    .pointerInput(Unit){
                        detectTapGestures(
                            onDoubleTap = {isExpanded = !isExpanded}
                        )
                    }

                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { distance: Float ->
                            val distanceDp = with(density) { distance.toDp() }
                            widthDp = max(minWidth, (min(maxWidth, widthDp - distanceDp)))
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .width(10.dp)
                        .background(
                            color = daggerColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }

            LazyColumn(

                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(logsPanelBackground)
            ) {
                if (showLogs){
                    var logs = Logger.logs
                    items(logs.size) { index ->
                        val log = logs[index]
                        val (prefix, color) = when (log.logType) {
                            Logger.LogType.ERROR -> "[ERROR]" to errorTextColor
                            Logger.LogType.TEXT -> "[INFO]" to logsTextColor
                        }

                        Text(
                            text = "$prefix ${log.message}",
                            color = color,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                }

            }
        }
    }


}