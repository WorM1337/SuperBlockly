package com.unewexp.superblockly.viewBlocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.R
import com.unewexp.superblockly.Spinner
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.list.AddElementByIndex
import com.unewexp.superblockly.blocks.list.EditValueByIndex
import com.unewexp.superblockly.blocks.list.FixedValuesAndSizeList
import com.unewexp.superblockly.blocks.list.GetValueByIndex
import com.unewexp.superblockly.blocks.list.PushBackElement
import com.unewexp.superblockly.blocks.list.RemoveValueByIndex
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.BooleanLogicBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.blocks.loops.ForBlock
import com.unewexp.superblockly.blocks.loops.ForElementInListBlock
import com.unewexp.superblockly.blocks.returnBlocks.StringConcatenationBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.BooleanLogicType
import com.unewexp.superblockly.enums.CompareType
import com.unewexp.superblockly.enums.OperandType
import com.unewexp.superblockly.enums.symbol
import com.unewexp.superblockly.ui.theme.EmptySpace
import com.unewexp.superblockly.ui.theme.innerBlockField
import com.unewexp.superblockly.ui.theme.textColor
import com.unewexp.superblockly.ui.theme.textFieldColorForCard
import com.unewexp.superblockly.ui.theme.backgoundForCard
import com.unewexp.superblockly.ui.theme.spinnerColor

@Composable
fun StartBlockView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        color = innerBlockField,
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.core),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
fun TextFieldLike(
    value: String = "",
    placeholder: String = "Num",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .height(TextFieldDefaults.MinHeight)
            .background(
                color = textFieldColorForCard,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun IntLiteralView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState(block.block as IntLiteralBlock)
    var value by remember { mutableStateOf(TextFieldValue(currentBlock.value.toString())) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.text.isEmpty()) {
                    currentBlock.value = 0
                    value = TextFieldValue("0", TextRange(1))
                } else if (it.text.matches(Regex("^\\d*$"))) {
                    if (it.text.length != 1 && it.text[0] == '0') {
                        currentBlock.value = it.text.substring(1).toInt()
                        value = it.copy(currentBlock.value.toString(), TextRange(1))
                    } else {
                        currentBlock.value = it.text.toInt()
                        value = it.copy(currentBlock.value.toString())
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            stringResource(R.string.value),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun IntLiteralViewForCard(){
    TextFieldLike(placeholder = stringResource(R.string.Num), modifier = Modifier.fillMaxWidth().background(backgoundForCard))
}

@Composable
fun BooleanLiteralBlockView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState(block.block as BooleanLiteralBlock)
    var value by remember { mutableStateOf(TextFieldValue(currentBlock.value.toString())) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                currentBlock.value = it.text.trim().toBoolean()
                value = it
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            stringResource(R.string.value),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun BooleanLiteralBlockViewForCard(){
    TextFieldLike(placeholder = stringResource(R.string.Boolean), modifier = Modifier.fillMaxWidth().background(backgoundForCard))
}

@Composable
fun StringLiteralBlockView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState(block.block as StringLiteralBlock)
    var value by remember { mutableStateOf(TextFieldValue(currentBlock.value)) }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        BasicTextField(
            value = value,
            onValueChange = {
                currentBlock.value = it.text
                value = it.copy(currentBlock.value)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.text.isEmpty()) {
                        Text(
                            stringResource(R.string.text),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun StringLiteralBlockViewForCard(){
    TextFieldLike(placeholder = stringResource(R.string.text), modifier = Modifier.fillMaxWidth().background(backgoundForCard))
}

@Composable
fun StringConcatenationBlockView(
    block: DraggableBlock,
) {

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as StringConcatenationBlock).leftInputConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as StringConcatenationBlock).rightInputConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(box1Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.plus),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .width(box2Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun StringConcatenationBlockForCard() {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = stringResource(R.string.plus),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun SetValueVariableView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState(block.block as SetValueVariableBlock)
    var name by remember { mutableStateOf(TextFieldValue(currentBlock.selectedVariableName)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                stringResource(R.string.set),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )

            BasicTextField(
                value = name,
                onValueChange = {
                    currentBlock.selectedVariableName = it.text.trim()
                    name = it.copy(currentBlock.selectedVariableName)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .align(Alignment.CenterVertically),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (name.text.isEmpty()) {
                            Text(
                                stringResource(R.string.Var),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Text(
                stringResource(R.string.to),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun SetValueVariableViewForCard(){

    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            stringResource(R.string.set),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextFieldLike(placeholder = stringResource(R.string.Var), modifier = Modifier.width(150.dp).background(backgoundForCard))
        Text(
            stringResource(R.string.to),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}

@Composable
fun VariableReferenceView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState( block.block as VariableReferenceBlock)
    var name by remember { mutableStateOf(TextFieldValue(currentBlock.selectedVariableName)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        BasicTextField(
            value = name,
            onValueChange = {
                currentBlock.selectedVariableName = it.text.trim()
                name = it.copy(currentBlock.selectedVariableName)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (name.text.isEmpty()) {
                        Text(
                            stringResource(R.string.Var),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                }
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun VariableReferenceViewForCard(){
    TextFieldLike(placeholder = stringResource(R.string.Var), modifier = Modifier.fillMaxWidth().background(backgoundForCard))
}

@Composable
fun VariableDeclarationBlockView(
    block: DraggableBlock
) {
    val currentBlock by rememberUpdatedState( block.block as VariableDeclarationBlock)
    var name by remember { mutableStateOf(TextFieldValue(currentBlock.name)) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
        ) {
            Text(
                stringResource(R.string.init),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )

            BasicTextField(
                value = name,
                onValueChange = {
                    currentBlock.name = it.text.trim()
                    name = it.copy(currentBlock.name)
                },
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .align(Alignment.CenterVertically),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (name.text.isEmpty()) {
                            Text(
                                stringResource(R.string.Var),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun DeclarationVariableViewForCard(){
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            stringResource(R.string.init),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextFieldLike(placeholder = stringResource(R.string.Var), modifier = Modifier.fillMaxWidth().background(backgoundForCard))
    }
}

@Composable
fun BooleanLogicBlockView(
    block: DraggableBlock,
    onBooleanLogicSelected: (BooleanLogicType) -> Unit
) {

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as BooleanLogicBlock).leftInputConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as BooleanLogicBlock).rightInputConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    var selectedLogicType by remember { mutableStateOf(BooleanLogicType.AND) }
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(box1Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Spinner(
                    size = 50.dp,
                    onClick = { expanded = true }
                ) {
                    Text(
                        text = selectedLogicType.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    BooleanLogicType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedLogicType = type
                                onBooleanLogicSelected(type)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(box2Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun BooleanLogicBlockForCard(
    booleanLogicType: BooleanLogicType = BooleanLogicType.AND,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = booleanLogicType.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun CompareNumbersBlockView(
    block: DraggableBlock,
    onCompareSelected: (CompareType) -> Unit
) {

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as CompareNumbers).leftInputConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as CompareNumbers).rightInputConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    var selectedOperand by remember { mutableStateOf(CompareType.EQUAL) }
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(box1Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Spinner(
                    size = 50.dp,
                    onClick = { expanded = true }
                ) {
                    Text(
                        text = selectedOperand.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    CompareType.entries.forEach { operand ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    operand.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedOperand = operand
                                onCompareSelected(operand)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(box2Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun CompareNumbersBlockForCard(
    compareType: CompareType = CompareType.EQUAL,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = compareType.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun NotBlockView(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            stringResource(R.string.not),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun NotBlockViewForCard(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            stringResource(R.string.not) + " " + stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun IfBlockView() {
    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    Box(
        modifier = Modifier
            .width(60.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .height(textHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    stringResource(R.string.If),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 32.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(lineWidth)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .height(bottomLineHeight)
                    .width(bottomLineWidth)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun IfBlockViewForCard(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.If),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun ElseBlockView() {
    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    Box(
        modifier = Modifier
            .width(60.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .height(textHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    stringResource(R.string.Else),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 32.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(lineWidth)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .height(bottomLineHeight)
                    .width(bottomLineWidth)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun ElseBlockViewForCard(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.Else),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun ElseIfBlockView() {
    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    Box(
        modifier = Modifier
            .width(60.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .height(textHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    stringResource(R.string.else_if),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 32.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(lineWidth)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .height(bottomLineHeight)
                    .width(bottomLineWidth)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun ElseIfBlockViewForCard(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.else_if),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun PrintBlockView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.print),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun OperandBlockView(
    onOperandSelected: (OperandType) -> Unit,
    block: DraggableBlock
) {
    var selectedOperand by remember { mutableStateOf(OperandType.PLUS) }
    var expanded by remember { mutableStateOf(false) }

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as OperandBlock).leftInputConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as OperandBlock).rightInputConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(box1Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Box(
                contentAlignment = Alignment.Center
            ) {
                Spinner(
                    size = 50.dp,
                    onClick = { expanded = true }
                ) {
                    Text(
                        text = selectedOperand.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(spinnerColor)
                ) {
                    OperandType.entries.forEach { operand ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    operand.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            onClick = {
                                selectedOperand = operand
                                onOperandSelected(operand)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .width(box2Width)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun OperandBlockForCard(
    operandType: OperandType = OperandType.PLUS,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgoundForCard,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = textFieldColorForCard,
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(50.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = operandType.symbol(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(70.dp)
                    .fillMaxHeight()
                    .background(
                        color = textFieldColorForCard,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
fun WhileBlockView() {
    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    Box(
        modifier = Modifier
            .width(80.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .height(textHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    stringResource(R.string.While),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 32.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(lineWidth)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .height(bottomLineHeight)
                    .width(bottomLineWidth)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun WhileBlockViewForCard(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.While),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun ForBlockView(
    block: DraggableBlock
) {

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): List<DraggableBlock?> {
        var refs: List<DraggableBlock?> = listOf(null, null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as ForBlock).initialValueBlock.connectedTo){
                refs = listOf(it, null, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as ForBlock).maxValueBlock.connectedTo){
                refs = listOf(refs.first(), it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as ForBlock).stepBlock.connectedTo){
                refs = listOf(refs.first(), refs[1], it)
            }
        }
        return refs
    }

    val (leftBlock, centerBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { centerBlock?.width?.value ?: 60.dp }
    val box3Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    val currentBlock by rememberUpdatedState(block.block as ForBlock)
    var name by remember { mutableStateOf(TextFieldValue(currentBlock.variableName)) }

    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .height(textHeight)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                stringResource(R.string.For),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            BasicTextField(
                value = name,
                onValueChange = {
                    currentBlock.variableName = it.text.trim()
                    name = it.copy(currentBlock.variableName)
                },
                modifier = Modifier
                    .height(24.dp)
                    .width(100.dp)
                    .padding(4.dp, 0.dp)
                    .align(Alignment.CenterVertically),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth().border(1.dp, EmptySpace, RoundedCornerShape(8.dp)).padding(2.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (name.text.isEmpty()) {
                            Text(
                                stringResource(R.string.Var),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Text(
                stringResource(R.string.from),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.weight(1f).padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(box1Width)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace),
                contentAlignment = Alignment.Center
            ){
                Text(
                    stringResource(R.string.from),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Text(
                stringResource(R.string.to),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.weight(1f).padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(box2Width)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace),
                contentAlignment = Alignment.Center
            ){
                Text(
                    stringResource(R.string.to),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Text(
                stringResource(R.string.step),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.weight(1f).padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(box3Width)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace),
                contentAlignment = Alignment.Center
            ){
                Text(
                    stringResource(R.string.step),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .width(lineWidth)
                .background(Color.White)
        )

        Box(
            modifier = Modifier
                .height(bottomLineHeight)
                .width(bottomLineWidth)
                .background(Color.White)
        )
    }
}

@Composable
fun ForBlockViewForCard() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.For),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun ForElementInListBlockView(block: DraggableBlock) {
    val textHeight = 60.dp
    val bottomLineHeight = 1.dp
    val lineWidth = 1.dp
    val bottomLineWidth = 60.dp

    val currentBlock by rememberUpdatedState(block.block as ForElementInListBlock)
    var name by remember { mutableStateOf(TextFieldValue(currentBlock.elementName)) }

    Box(
        modifier = Modifier
            .width(200.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier
                    .height(textHeight)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    stringResource(R.string.For),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                BasicTextField(
                    value = name,
                    onValueChange = {
                        currentBlock.elementName = it.text.trim()
                        name = it.copy(currentBlock.elementName)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(24.dp)
                        .padding(4.dp, 0.dp)
                        .align(Alignment.CenterVertically),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth().border(1.dp, EmptySpace, RoundedCornerShape(8.dp)).padding(2.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (name.text.isEmpty()) {
                                Text(
                                    stringResource(R.string.Var),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                Text(
                    stringResource(R.string.in_list),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .width(lineWidth)
                    .background(Color.White)
            )

            Box(
                modifier = Modifier
                    .height(bottomLineHeight)
                    .width(bottomLineWidth)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun ForElementInListBlockViewForCard(
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            stringResource(R.string.For) + " " + stringResource(R.string.items) + " " + stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun FixedValuesAndSizeListView(block: DraggableBlock){

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as FixedValuesAndSizeList).valueInput.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as FixedValuesAndSizeList).repeatTimes.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            stringResource(R.string.create_list_with),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.value),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.repeated),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box2Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.count),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.times),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun FixedValuesAndSizeListViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){

        Text(
            stringResource(R.string.create_list_with),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )

        Text(
            stringResource(R.string.repeated),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )

        Text(
            "N",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun AddElementByIndexView(block: DraggableBlock){

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as AddElementByIndex).listConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as AddElementByIndex).idConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.list),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.insert),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box2Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.id),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun AddElementByIndexViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
        Text(
            stringResource(R.string.insert),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun GetValueByIndexView(block: DraggableBlock){

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as GetValueByIndex).listConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as GetValueByIndex).idConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.list),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.get),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box2Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.id),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun GetValueByIndexViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
        Text(
            stringResource(R.string.get) + " " + stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun GetListSizeView(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            stringResource(R.string.length_of),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun GetListSizeViewForCard(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            stringResource(R.string.length_of)+ " " + stringResource(R.string.list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun RemoveValueByIndexView(block: DraggableBlock){

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as RemoveValueByIndex).listConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as RemoveValueByIndex).idConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.list),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.remove),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box2Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.id),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun RemoveValueByIndexViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
        Text(
            stringResource(R.string.remove) + " " + stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun EditValueByIndexView(block: DraggableBlock){

    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): Pair<DraggableBlock?, DraggableBlock?> {
        var pair: Pair<DraggableBlock?, DraggableBlock?> = Pair(null, null)
        blocks.forEach { it ->
            if (it.block == (block.block as EditValueByIndex).listConnector.connectedTo){
                pair = Pair(it, null)
            }
        }
        blocks.forEach { it ->
            if (it.block == (block.block as EditValueByIndex).idConnector.connectedTo){
                pair = pair.copy(second = it)
            }
        }
        return pair
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.list),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.edit),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box2Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.id),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }
        Text(
            stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun EditValueByIndexViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
        Text(
            stringResource(R.string.edit),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Composable
fun PushBackElementView(block: DraggableBlock){
    fun determineBlocks(blocks: SnapshotStateList<DraggableBlock>): DraggableBlock? {
        blocks.forEach { it ->
            if (it.block == (block.block as PushBackElement).listConnector.connectedTo){
                return it
            }
        }
        return null
    }

    val leftBlock = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(box1Width)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){
            Text(
                stringResource(R.string.list),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black.copy(alpha = 0.5f), fontSize = 14.sp),
                modifier = Modifier.padding(4.dp)
            )
        }

        Text(
            stringResource(R.string.push_back),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun PushBackElementViewForCard(){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
        Text(
            stringResource(R.string.push_back) + " " + stringResource(R.string.value),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black, fontSize = 14.sp),
            modifier = Modifier.padding(2.dp)
        )
    }
}