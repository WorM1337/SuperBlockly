package com.unewexp.superblockly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.viewBlocks.DeclarationVariableViewForCard
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IfBlockViewForCard
import com.unewexp.superblockly.viewBlocks.IntLiteralViewForCard
import com.unewexp.superblockly.viewBlocks.SetValueVariableViewForCard
import com.unewexp.superblockly.viewBlocks.VariableReferenceViewForCard

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
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(2.dp)
    ){
        IntLiteralViewForCard()
    }
}

@Composable
fun SetValueVariableCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(2.dp)
    ){
        SetValueVariableViewForCard()
    }
}

@Composable
fun DeclarationVariableCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(2.dp)
    ){
        DeclarationVariableViewForCard()
    }
}

@Composable
fun ReferenceVariableCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(2.dp)
    ){
        VariableReferenceViewForCard()
    }
}

@Composable
fun IfBlockCard(){
    Card(
        modifier = Modifier
            .width(100.dp)
            .padding(2.dp)
    ){
        IfBlockViewForCard()
    }
}