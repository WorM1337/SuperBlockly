package com.example.myfirstapplicatioin.viewBlocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.VariableBlock
import com.example.myfirstapplicatioin.model.connectionView
import kotlin.math.roundToInt

class ViewVariableBlock(
    val variableBlock: VariableBlock,
    initialX: Dp,
    initialY: Dp
) : ViewBlock(variableBlock, initialX, initialY) {

    // Список соединителей
    override val listConnectors: List<СonnectionView> = listOf(
        connectionView(variableBlock.valueConnector, initialX, initialY)
    )

    @Composable
    override fun render() {
        _render({
            var name by remember { mutableStateOf(TextFieldValue(variableBlock.name)) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Set",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 4.dp)
                )

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Var") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    textStyle = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "To",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        })
    }

}
