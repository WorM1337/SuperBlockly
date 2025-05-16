package com.example.myfirstapplicatioin.viewBlocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.viewBlocks.InlineViewBlock

class ViewIntLiteralBlock(
    initialX: Dp,
    initialY: Dp
) : InlineViewBlock(initialX, initialY) {

    var literalBlock = IntLiteralBlock(0)
    override var listConnectors: List<ConnectionView> = listOf(ConnectionView(literalBlock.outputConnector, initialX, initialY))

    private val modifier = Modifier
        .size(100.dp, 40.dp)
        .background(Color(0xFFE0E0E0))
        .padding(horizontal = 8.dp, vertical = 4.dp)

    @Composable
    private fun Content(enabled: Boolean = true){

        var name by remember { mutableStateOf(TextFieldValue(literalBlock.value.toString())) }

        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Var") },
            singleLine = true,
            modifier = Modifier
                .padding(horizontal = 4.dp),
            textStyle = MaterialTheme.typography.bodySmall
        )
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
        _render({ Content() }, 100.dp, 60.dp)
    }

}