package com.unewexp.superblockly

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.viewBlocks.ViewBlock
import com.unewexp.superblockly.viewBlocks.TestViewForCard

@Composable
fun BlockCard(content: @Composable () -> Unit, onClick: () -> Unit){
    Card(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        content()
    }
}

@Composable
fun IntLiteralBlockCard(){
    Card{
        TestViewForCard()
    }
}

