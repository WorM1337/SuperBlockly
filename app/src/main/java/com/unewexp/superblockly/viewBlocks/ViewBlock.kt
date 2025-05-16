//package com.example.myfirstapplicatioin.viewBlocks
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.IntOffset
//import androidx.compose.ui.unit.dp
//import com.example.myfirstapplicatioin.blocks.Block
//import com.example.myfirstapplicatioin.model.connectionView
//import kotlin.math.roundToInt
//
//abstract class ViewBlock(
//    val block: Block,
//    var initialX: Dp,
//    var initialY: Dp
//) {
//
//    abstract val listConnectors: List<СonnectionView>
//
//    @Composable
//    protected fun _render( content: @Composable () -> Unit){
//        val density = LocalDensity.current
//
//        // Переводим Dp в пиксели один раз при старте
//        val initialPxX = with(density) { initialX.toPx() }
//        val initialPxY = with(density) { initialY.toPx() }
//
//        // Состояния для текущей позиции в пикселях
//        var offsetX by remember { mutableStateOf(initialPxX) }
//        var offsetY by remember { mutableStateOf(initialPxY) }
//
//        Box(
//            modifier = Modifier
//                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
//                .size(200.dp, 60.dp)
//                .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
//                .pointerInput(Unit) {
//                    detectDragGestures { _, dragAmount ->
//                        offsetX += dragAmount.x
//                        offsetY += dragAmount.y
//                    }
//                }
//                .padding(horizontal = 8.dp, vertical = 4.dp)
//        ) {
//            content()
//        }
//    }
//    @Composable
//    abstract fun render()
//}