package com.unewexp.superblockly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
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


@Composable
fun ConsolePanel(
    height: Dp
){
    val logs = Logger.logs
    var isExpanded by remember{ mutableStateOf(false) }
    var widthDp by remember { mutableStateOf(300.dp) }

    val minWidth = 50.dp
    val maxWidth = 650.dp

    val density = LocalDensity.current

    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Box(
        modifier = Modifier
            .height(height)
            .wrapContentWidth(Alignment.End)
            .width(widthDp)
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
                    .width(20.dp)
                    .background(
                        color = Color(0xFF2C2C2C),  // фон полосы ресайза
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                        )
                    )
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
                            color = Color(0xFF555555),  // маленький прямоугольник (драггер)
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(Color(0xFF1E1E1E))  // фон логов
            ) {
                items(logs.size) { index ->
                    val log = logs[index]
                    val (prefix, color) = when (log.logType) {
                        Logger.LogType.ERROR -> "[ERROR]" to Color(0xFFFF5555) // красный для ошибок
                        Logger.LogType.TEXT -> "[INFO]" to Color(0xFFE0E0E0)  // светло-серый для обычных
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