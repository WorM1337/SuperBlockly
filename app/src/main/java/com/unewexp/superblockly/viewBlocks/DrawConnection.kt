package com.unewexp.superblockly.viewBlocks

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.ui.theme.ConnectorColor

@Composable
fun TopConnector(
    modifier: Modifier = Modifier,
    isConnected: Boolean
){
    val connectorWidth = 20.dp
    val connectorHeight = 16.dp

    val color by remember(isConnected) {
        derivedStateOf {
            if (isConnected) ConnectorColor
            else Color.Gray.copy(alpha = 0.5f)
        }
    }

    Box(
        modifier = modifier
            .size(width = connectorWidth, height = connectorHeight),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier.size(connectorWidth, connectorHeight)
        ) {

            val path = Path().apply {
                addRect(Rect(Offset.Zero, size))
            }

            drawPath(
                path = path,
                color = color,
                style = Fill
            )
        }
    }
}

@Composable
fun BottomConnector(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF2069B8),
    isConnected: Boolean = false
) {
    val connectorWidth = 48.dp
    val connectorHeight = 24.dp

    Box(
        modifier = modifier
            .size(width = connectorWidth, height = connectorHeight),
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(
            modifier = Modifier.size(connectorWidth, connectorHeight)
        ) {
            val width = size.width
            val height = size.height
            val cornerRadius = height / 2

            val path = Path().apply {
                arcTo(
                    rect = Rect(Offset(0f, -cornerRadius), Size(cornerRadius * 2, height)),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                arcTo(
                    rect = Rect(Offset(2*cornerRadius, -cornerRadius), Size(cornerRadius * 2, height)),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 180f,
                    forceMoveTo = false
                )
                close()
            }

            drawPath(
                path = path,
                color = color,
                style = Fill
            )

            val path2 = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, height/2)
                moveTo(width, 0f)
                lineTo(width, height/2)
                close()
            }

            drawPath(
                path = path2,
                color = color,
                style = Stroke(width = 2f)
            )
        }
    }
}