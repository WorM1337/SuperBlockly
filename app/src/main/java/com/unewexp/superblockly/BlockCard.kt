package com.unewexp.superblockly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.viewBlocks.DeclarationVariableViewForCard
import com.unewexp.superblockly.viewBlocks.ElseBlockViewForCard
import com.unewexp.superblockly.viewBlocks.ElseIfBlockViewForCard
import com.unewexp.superblockly.viewBlocks.FixedValuesAndSizeListViewForCard
import com.unewexp.superblockly.viewBlocks.ForBlockViewForCard
import com.unewexp.superblockly.viewBlocks.IfBlockViewForCard
import com.unewexp.superblockly.viewBlocks.IntLiteralViewForCard
import com.unewexp.superblockly.viewBlocks.OperandBlockForCard
import com.unewexp.superblockly.viewBlocks.PrintBlockView
import com.unewexp.superblockly.viewBlocks.SetValueVariableViewForCard
import com.unewexp.superblockly.viewBlocks.VariableReferenceViewForCard
import com.unewexp.superblockly.viewBlocks.WhileBlockViewForCard

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
fun IntLiteralBlockCard(alpha: Float = 1f){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
            .alpha(alpha)
    ){
        IntLiteralViewForCard()
    }
}

@Composable
fun SetValueVariableCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
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
            .height(60.dp)
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
            .height(60.dp)
            .padding(2.dp)
    ){
        VariableReferenceViewForCard()
    }
}

@Composable
fun IfBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        IfBlockViewForCard()
    }
}

@Composable
fun ElseBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        ElseBlockViewForCard()
    }
}

@Composable
fun ElseIfBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        ElseIfBlockViewForCard()
    }
}

@Composable
fun PrintBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        PrintBlockView()
    }
}

@Composable
fun OperandBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        OperandBlockForCard()
    }
}

@Composable
fun WhileBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        WhileBlockViewForCard()
    }
}

@Composable
fun ForBlockCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        ForBlockViewForCard()
    }
}

@Composable
fun FixedValuesAndSizeListViewCard(){
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
            .padding(2.dp)
    ){
        FixedValuesAndSizeListViewForCard()
    }
}