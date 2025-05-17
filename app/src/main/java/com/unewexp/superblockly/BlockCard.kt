package com.unewexp.superblockly

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.myfirstapplicatioin.viewBlocks.ViewBlock

@Composable
fun BlockCard(block: ViewBlock){
    Card {
        block.View()
    }
}
