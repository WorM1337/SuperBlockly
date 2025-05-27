package com.unewexp.superblockly.viewBlocks.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
            moveTo(sideOffset, 0f)                  // Верхний левый угол
            lineTo(width - sideOffset, 0f)          // Верхний правый угол
            lineTo(width, height)                   // Нижний правый угол
            lineTo(0f, height)                      // Нижний левый угол
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

