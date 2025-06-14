package com.unewexp.superblockly.viewBlocks.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SoftTriangle() {
    var topWidthRatio = 0.3f
    Canvas(
        modifier = Modifier.width(40.dp)
            .height(10.dp)
    ) {
        val width = size.width
        val height = size.height

        val topWidth = width * topWidthRatio
        val sideOffset = (width - topWidth) / 2

        val path = Path().apply {
            moveTo(sideOffset, 0f)
            lineTo(width - sideOffset, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }

        drawPath(path = path, color = Color.Red)
    }
}

@Composable
fun BottomCorner(){

}

@Composable
fun InputCorner(){

}

@Composable
fun OutputCorner(){

}

