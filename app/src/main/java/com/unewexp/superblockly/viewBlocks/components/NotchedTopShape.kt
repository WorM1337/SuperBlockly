package com.unewexp.superblockly.viewBlocks.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class NotchedTopShape(
    private val topWidth: Dp = 40.dp,
    private val bottomWidth: Dp = 30.dp,
    private val notchHeight: Dp = 20.dp,
    private val offsetX: Dp = 10.dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val notchWidthTopPx = with(density) { topWidth.toPx() }
        val notchWidthBottomPx = with(density) { bottomWidth.toPx() }
        val notchHeightPx = with(density) { notchHeight.toPx() }
        val offsetXPx = with(density) { offsetX.toPx() }

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()


            val leftTop = offsetXPx
            val rightTop = offsetXPx + notchWidthTopPx
            val rightBottom = offsetXPx + (notchWidthTopPx - notchWidthBottomPx) / 2 + notchWidthBottomPx
            val leftBottom = offsetXPx + (notchWidthTopPx - notchWidthBottomPx) / 2

            moveTo(leftTop, 0f)
            lineTo(leftBottom, notchHeightPx)
            lineTo(rightBottom, notchHeightPx)
            lineTo(rightTop, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}


