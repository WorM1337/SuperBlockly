package com.unewexp.superblockly.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp


@Composable
fun DebugPanel() {
    var isExpanded by remember { mutableStateOf(true) }

    var widthDp by remember { mutableStateOf(216.dp) }
    val rolledUp = 16.dp

    val minPanelWidth = 200.dp
    val maxPanelWidth = 500.dp

    val minConsoleWidth = 70.dp
    val density = LocalDensity.current

    val leftPanelColor = Color(0xFF2E2B3F)
    val rightPanelColor = Color(0xFF1E2B38)

    var leftFraction by remember { mutableStateOf(0.5f) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(Alignment.End)
            .width(if (isExpanded) widthDp else rolledUp)
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
            )
    ) {
        Row(modifier = Modifier.fillMaxSize()) {

            // Левая полоса (разворачивание)
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(rolledUp)
                    .background(
                        color = Color(0xFF2C2C2C),
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = { isExpanded = !isExpanded }
                        )
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { distance ->
                            val distanceDp = with(density) { distance.toDp() }
                            widthDp = (widthDp - distanceDp).coerceIn(minPanelWidth, maxPanelWidth)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .width(10.dp)
                        .background(Color(0xFF555555), RoundedCornerShape(5.dp))
                )
            }

            if (isExpanded) {
                val usableWidth = widthDp - rolledUp * 2
                val leftWidth = max(minConsoleWidth, usableWidth * leftFraction)
                val rightWidth = usableWidth - leftWidth

                // Левая панель
                Box(
                    modifier = Modifier
                        .width(leftWidth)
                        .fillMaxHeight()
                        .background(leftPanelColor)
                ) {
                    VariablesPanel()
                }

                // Центр. полоса (перетаскиваемая)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(rolledUp)
                        .background(Color(0xFF2C2C2C))
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { distance ->
                                val usable = widthDp - rolledUp * 2
                                val deltaDp = with(density) { distance.toDp() }
                                val newLeft = (usable * leftFraction + deltaDp).coerceIn(minConsoleWidth, usable - minConsoleWidth)
                                leftFraction = newLeft / usable
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .width(10.dp)
                            .background(Color(0xFF555555), RoundedCornerShape(5.dp))
                    )
                }

                // Правая панель
                Box(
                    modifier = Modifier
                        .width(rightWidth)
                        .fillMaxHeight()
                        .background(rightPanelColor)
                ) {
                    LogsPanel()
                }
            }
        }
    }
}


@Composable
fun VariablesPanel() {
    var scopes = ExecutionContext.scopes
    Box(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(Color(0xFF1E1E1E))
            .padding(12.dp)
    ) {
        VariableList(scopes)
    }
}

@Composable
fun VariableList(scopes: List<Map<String, Any?>>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        scopes.forEachIndexed { scopeIndex, scopeMap ->
            itemsIndexed(scopeMap.entries.toList()) { _, entry ->
                VariableRow(
                    name = entry.key,
                    value = entry.value,
                    indentLevel = scopeIndex
                )
            }
        }
    }
}

@Composable
fun VariableRow(name: String, value: Any?, indentLevel: Int) {
    val typeName = value?.let { it::class.simpleName } ?: "null"
    val displayValue = value?.toString() ?: "null"
    val valueColor = when (value) {
        is String -> Color(0xFF81C784)
        is Int, is Float, is Double -> Color(0xFF64B5F6)
        is Boolean -> Color(0xFFFFD54F)
        null -> Color(0xFFEF9A9A)
        else -> Color(0xFFE0E0E0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (indentLevel * 16).dp, bottom = 6.dp)
    ) {
        Text(
            text = "$name: $typeName = ",
            fontWeight = FontWeight.Medium,
            color = Color(0xFFB0BEC5),
            fontSize = 14.sp,
            modifier = Modifier.width(140.dp)
        )
        Text(
            text = displayValue,
            color = valueColor,
            fontSize = 14.sp
        )
    }
}

@Composable()
fun LogsPanel(){

    var logs = Logger.logs
    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()

    ) {
        items(logs.size) { index ->
            val log = logs[index]
            val (prefix, color) = when (log.logType) {
                Logger.LogType.ERROR -> "[ERROR]" to Color(0xFFFF5555)
                Logger.LogType.TEXT -> "[INFO]" to Color(0xFFE0E0E0)
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