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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.unewexp.superblockly.ui.theme.booleanLogVariableColor
import com.unewexp.superblockly.ui.theme.daggerColor
import com.unewexp.superblockly.ui.theme.errorTextColor
import com.unewexp.superblockly.ui.theme.logDraggableLine
import com.unewexp.superblockly.ui.theme.logsPanelBackground
import com.unewexp.superblockly.ui.theme.logsTextColor
import com.unewexp.superblockly.ui.theme.nullLogVariableColor
import com.unewexp.superblockly.ui.theme.numberLogVariableColor
import com.unewexp.superblockly.ui.theme.otherLogVariableColor
import com.unewexp.superblockly.ui.theme.stringLogVariableColor

import com.unewexp.superblockly.ui.theme.variablesDebugColor


@Composable
fun DebugPanel() {
    var isExpanded by remember { mutableStateOf(true) }

    var widthDp by remember { mutableStateOf(216.dp) }
    val rolledUp = 16.dp

    val minPanelWidth = 200.dp
    val maxPanelWidth = 500.dp

    val minConsoleWidth = 70.dp
    val density = LocalDensity.current


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

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(rolledUp)
                    .background(
                        color = logDraggableLine,
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
                        .background(daggerColor, RoundedCornerShape(5.dp))
                )
            }

            if (isExpanded) {
                val usableWidth = widthDp - rolledUp * 2
                val leftWidth = max(minConsoleWidth, usableWidth * leftFraction)
                val rightWidth = usableWidth - leftWidth


                Box(
                    modifier = Modifier
                        .width(leftWidth)
                        .fillMaxHeight()
                        .background(variablesDebugColor)
                ) {
                    VariablesPanel()
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(rolledUp)
                        .background(logDraggableLine)
                        .draggable(
                            orientation = Orientation.Horizontal,
                            state = rememberDraggableState { distance ->
                                val usable = widthDp - rolledUp * 2
                                val deltaDp = with(density) { distance.toDp() }
                                val newLeft = (usable * leftFraction + deltaDp).coerceIn(
                                    minConsoleWidth,
                                    usable - minConsoleWidth
                                )
                                leftFraction = newLeft / usable
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .height(70.dp)
                            .width(10.dp)
                            .background(daggerColor, RoundedCornerShape(5.dp))
                    )
                }


                Box(
                    modifier = Modifier
                        .width(rightWidth)
                        .fillMaxHeight()
                        .background(logsPanelBackground)
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
        is String -> stringLogVariableColor
        is Int, is Float, is Double -> numberLogVariableColor
        is Boolean -> booleanLogVariableColor
        null -> nullLogVariableColor
        else -> otherLogVariableColor
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