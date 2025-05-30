package com.unewexp.superblockly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.viewBlocks.AddElementByIndexViewForCard
import com.unewexp.superblockly.viewBlocks.BooleanLiteralBlockViewForCard
import com.unewexp.superblockly.viewBlocks.BooleanLogicBlockForCard
import com.unewexp.superblockly.viewBlocks.CompareNumbersBlockForCard
import com.unewexp.superblockly.viewBlocks.DeclarationVariableViewForCard
import com.unewexp.superblockly.viewBlocks.EditValueByIndexViewForCard
import com.unewexp.superblockly.viewBlocks.ElseBlockViewForCard
import com.unewexp.superblockly.viewBlocks.ElseIfBlockViewForCard
import com.unewexp.superblockly.viewBlocks.FixedValuesAndSizeListViewForCard
import com.unewexp.superblockly.viewBlocks.ForBlockViewForCard
import com.unewexp.superblockly.viewBlocks.ForElementInListBlockViewForCard
import com.unewexp.superblockly.viewBlocks.GetListSizeViewForCard
import com.unewexp.superblockly.viewBlocks.GetValueByIndexViewForCard
import com.unewexp.superblockly.viewBlocks.IfBlockViewForCard
import com.unewexp.superblockly.viewBlocks.IntLiteralViewForCard
import com.unewexp.superblockly.viewBlocks.NotBlockViewForCard
import com.unewexp.superblockly.viewBlocks.OperandBlockForCard
import com.unewexp.superblockly.viewBlocks.PrintBlockView
import com.unewexp.superblockly.viewBlocks.PushBackElementViewForCard
import com.unewexp.superblockly.viewBlocks.RemoveValueByIndexViewForCard
import com.unewexp.superblockly.viewBlocks.SetValueVariableViewForCard
import com.unewexp.superblockly.viewBlocks.StringConcatenationBlockForCard
import com.unewexp.superblockly.viewBlocks.StringLiteralBlockViewForCard
import com.unewexp.superblockly.viewBlocks.VariableReferenceViewForCard
import com.unewexp.superblockly.viewBlocks.WhileBlockViewForCard

@Composable
fun BlockCard(content: @Composable () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .padding(2.dp)
            .width(200.dp)
            .height(60.dp)
    ) {
        content()
    }
}

@Composable
fun SimpleBlockCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .width(200.dp)
            .height(60.dp)
    ) {
        content()
    }
}

// Блоки

@Composable
fun IntLiteralBlockCard() {
    SimpleBlockCard { IntLiteralViewForCard() }
}

@Composable
fun BooleanLiteralBlockCard() {
    SimpleBlockCard { BooleanLiteralBlockViewForCard() }
}

@Composable
fun StringLiteralBlockCard() {
    SimpleBlockCard { StringLiteralBlockViewForCard() }
}

@Composable
fun StringConcatenationBlockCard() {
    SimpleBlockCard { StringConcatenationBlockForCard() }
}

@Composable
fun SetValueVariableCard() {
    SimpleBlockCard { SetValueVariableViewForCard() }
}

@Composable
fun DeclarationVariableCard() {
    SimpleBlockCard { DeclarationVariableViewForCard() }
}

@Composable
fun ReferenceVariableCard() {
    SimpleBlockCard { VariableReferenceViewForCard() }
}

@Composable
fun BooleanLogicBlockCard() {
    SimpleBlockCard { BooleanLogicBlockForCard() }
}

@Composable
fun CompareNumbersBlockCard() {
    SimpleBlockCard { CompareNumbersBlockForCard() }
}

@Composable
fun NotBlockCard() {
    SimpleBlockCard { NotBlockViewForCard() }
}

@Composable
fun IfBlockCard() {
    SimpleBlockCard { IfBlockViewForCard() }
}

@Composable
fun ElseBlockCard() {
    SimpleBlockCard { ElseBlockViewForCard() }
}

@Composable
fun ElseIfBlockCard() {
    SimpleBlockCard { ElseIfBlockViewForCard() }
}

@Composable
fun PrintBlockCard() {
    SimpleBlockCard { PrintBlockView() }
}

@Composable
fun OperandBlockCard() {
    SimpleBlockCard { OperandBlockForCard() }
}

@Composable
fun WhileBlockCard() {
    SimpleBlockCard { WhileBlockViewForCard() }
}

@Composable
fun ForBlockCard() {
    SimpleBlockCard { ForBlockViewForCard() }
}

@Composable
fun ForElementInLustBlockCard() {
    SimpleBlockCard { ForElementInListBlockViewForCard() }
}

@Composable
fun FixedValuesAndSizeListViewCard() {
    SimpleBlockCard { FixedValuesAndSizeListViewForCard() }
}

@Composable
fun AddElementByIndexViewCard() {
    SimpleBlockCard { AddElementByIndexViewForCard() }
}

@Composable
fun GetValueByIndexViewCard() {
    SimpleBlockCard { GetValueByIndexViewForCard() }
}

@Composable
fun GetListSizeViewCard() {
    SimpleBlockCard { GetListSizeViewForCard() }
}

@Composable
fun RemoveValueByIndexCard() {
    SimpleBlockCard { RemoveValueByIndexViewForCard() }
}

@Composable
fun EditValueByIndexCard() {
    SimpleBlockCard { EditValueByIndexViewForCard() }
}

@Composable
fun PushBackElementCard() {
    SimpleBlockCard { PushBackElementViewForCard() }
}
