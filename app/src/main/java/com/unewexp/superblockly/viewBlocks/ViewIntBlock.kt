package com.example.myfirstapplicatioin.viewBlocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.connectionView
import com.unewexp.superblockly.Routes
import com.unewexp.superblockly.viewBlocks.InlineViewBlock
import kotlin.math.roundToInt

class ViewIntBlock(
    initialX: Dp,
    initialY: Dp
) : InlineViewBlock(initialX, initialY) {

    var literalBlock = IntLiteralBlock(mutableStateOf(0))
    override var listConnectors: List<connectionView> = listOf(connectionView(literalBlock.outputConnector, initialX, initialY))

    private val modifier = Modifier
        .size(100.dp, 40.dp)
        .background(Color(0xFFE0E0E0))
        .padding(horizontal = 8.dp, vertical = 4.dp)

    @Composable
    private fun Content(enabled: Boolean = true){

        val text by remember(literalBlock.value.value) {
            mutableStateOf(literalBlock.value.value.toString())
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                //enabled = enabled,
                value = TextFieldValue(text),
                onValueChange = { newText ->
                    newText.text.toIntOrNull()?.let {
                        literalBlock.value.value = it
                    }
                    Log.i("sss", literalBlock.value.value.toString())
                },
                placeholder = { Text("Value") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                textStyle = MaterialTheme.typography.bodySmall
            )
        }
    }

    @Composable
    override fun _render( content: @Composable () -> Unit, width: Dp, height: Dp){
        val density = LocalDensity.current

        val initialPxX = with(density) { initialX.toPx() }
        val initialPxY = with(density) { initialY.toPx() }

        var offsetX by remember { mutableStateOf(initialPxX) }
        var offsetY by remember { mutableStateOf(initialPxY) }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(width, height)
                .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            content()
        }
    }

    @Composable
    override fun View(){
        Box(
            modifier = modifier
        ) {
            Content(false)
        }
    }

    @Composable
    override fun render() {
        val density = LocalDensity.current

        val initialPxX = with(density) { initialX.toPx() }
        val initialPxY = with(density) { initialY.toPx() }

        var offsetX by remember { mutableStateOf(initialPxX) }
        var offsetY by remember { mutableStateOf(initialPxY) }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(100.dp, 40.dp)
                .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Content()
        }
    }

}