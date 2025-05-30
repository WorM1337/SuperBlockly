package com.unewexp.superblockly.viewBlocks

import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.R
import com.unewexp.superblockly.Spinner
import com.unewexp.superblockly.enums.CompareType
import com.unewexp.superblockly.enums.OperandType
import com.unewexp.superblockly.enums.symbol
import com.unewexp.superblockly.ui.theme.EmptySpace

@Composable
fun StartBlockView() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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
                color = MaterialTheme.colorScheme.surface,
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
    onNameChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf(TextFieldValue("123")) }

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
                    value = TextFieldValue("0", TextRange(1))
                } else if (it.text.matches(Regex("^\\d*$"))) {
                    value = if (it.text.length != 1 && it.text[0] == '0') {
                        TextFieldValue(it.text.substring(1), TextRange(1))
                    } else {
                        it
                    }
                }
                onNameChanged(value.text)
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
                            "Value",
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

    TextFieldLike(placeholder = "Num", modifier = Modifier.fillMaxWidth())
}

@Composable
fun SetValueVariableView(
    onNameChanged: (String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

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
                "Set",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )

            BasicTextField(
                value = name,
                onValueChange = {
                    name = it
                    onNameChanged(name.text)
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
                                "Var",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Text(
                "to",
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
            "Set",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextFieldLike(placeholder = "Var", modifier = Modifier.width(150.dp))
        Text(
            stringResource(R.string.to),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
    }
}

@Composable
fun VariableReferenceView(
    onNameChanged: (String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        BasicTextField(
            value = name,
            onValueChange = {
                name = if (it.text.isEmpty()) TextFieldValue("") else it
                onNameChanged(name.text)
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
                            "Var",
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
    TextFieldLike(placeholder = "Var", modifier = Modifier.fillMaxWidth())
}

@Composable
fun DeclarationVariableView(
    onNameChanged: (String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

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
                "Init",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 8.dp)
            )

            TextField(
                value = name,
                onValueChange = {
                    name = it
                    onNameChanged(name.text)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                placeholder = {
                    Text(
                        "Var",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun DeclarationVariableViewForCard(){
    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            "Init",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextFieldLike(placeholder = "Var", modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun CompareNumbersBlockView(
    onCompareSelected: (CompareType) -> Unit = {}
) {
    var selectedOperand by remember { mutableStateOf(CompareType.EQUAL) }
    var expanded by remember { mutableStateOf(false) }

    val box1Width = remember { mutableStateOf(70.dp) }
    val box2Width = remember { mutableStateOf(70.dp) }

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
                    .width(box1Width.value)
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
                    .width(box2Width.value)
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
        return when (blocks.size) {
            0 -> Pair(null, null)
            1 -> {
                val singleBlock = blocks.first()
                val isLeft = singleBlock.x.value < block.x.value + block.width.value.value / 2
                if (isLeft) Pair(singleBlock, null) else Pair(null, singleBlock)
            }
            else -> {
                val sortedBlocks = blocks.sortedBy { it.x.value }
                Pair(sortedBlocks.first(), sortedBlocks.last())
            }
        }
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
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f),
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
    onNameChanged: (String) -> Unit
) {

    var name by remember { mutableStateOf(TextFieldValue("i")) }

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
                    name = it
                    onNameChanged(name.text)
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
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (name.text.isEmpty()) {
                            Text(
                                "Var",
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
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace)
            ){}

            Text(
                stringResource(R.string.to),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace)
            ){}

            Text(
                stringResource(R.string.step),
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp)
                    .padding(4.dp, 0.dp)
                    .background(EmptySpace)
            ){}
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
fun FixedValuesAndSizeListView(block: DraggableBlock){
    Row{

        Text(
            stringResource(R.string.create_list_with),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
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
            stringResource(R.string.repeated),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
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
        return when (blocks.size) {
            0 -> Pair(null, null)
            1 -> {
                val singleBlock = blocks.first()
                if(singleBlock.y.value > block.y.value + block.height.value.value / 2){
                    Pair(null, null)
                }
                else {
                    val isLeft = singleBlock.x.value < block.x.value + block.width.value.value / 2
                    if (isLeft) Pair(singleBlock, null) else Pair(null, singleBlock)
                }
            }
            2 -> {
                var sortedBlocks = blocks.sortedBy { it.y.value }
                if(sortedBlocks.first().y.value != sortedBlocks.last().y.value){
                    Pair(sortedBlocks.first(), null)
                }else{
                    sortedBlocks = blocks.sortedBy { it.x.value }
                    Pair(sortedBlocks.first(), sortedBlocks.last())
                }
            }
            else -> {
                var sortedBlocks = blocks.sortedBy { it.y.value }
                sortedBlocks = listOf(sortedBlocks.first(), sortedBlocks[1])
                sortedBlocks = sortedBlocks.sortedBy { it.x.value }
                Pair(sortedBlocks.first(), sortedBlocks.last())
            }
        }
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize()
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
    Row(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
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
                .width(60.dp)
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
        modifier = Modifier.fillMaxSize()
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
        return when (blocks.size) {
            0 -> Pair(null, null)
            1 -> {
                val singleBlock = blocks.first()
                if(singleBlock.y.value > block.y.value + block.height.value.value / 2){
                    Pair(null, null)
                }
                else {
                    val isLeft = singleBlock.x.value < block.x.value + block.width.value.value / 2
                    if (isLeft) Pair(singleBlock, null) else Pair(null, singleBlock)
                }
            }
            2 -> {
                var sortedBlocks = blocks.sortedBy { it.y.value }
                if(sortedBlocks.first().y.value != sortedBlocks.last().y.value){
                    Pair(sortedBlocks.first(), null)
                }else{
                    sortedBlocks = blocks.sortedBy { it.x.value }
                    Pair(sortedBlocks.first(), sortedBlocks.last())
                }
            }
            else -> {
                var sortedBlocks = blocks.sortedBy { it.y.value }
                sortedBlocks = listOf(sortedBlocks.first(), sortedBlocks[1])
                sortedBlocks = sortedBlocks.sortedBy { it.x.value }
                Pair(sortedBlocks.first(), sortedBlocks.last())
            }
        }
    }

    val (leftBlock, rightBlock) = remember(block.scope, block.scope.map { it.x.value }) {
        determineBlocks(block.scope)
    }

    val box1Width by derivedStateOf { leftBlock?.width?.value ?: 60.dp }
    val box2Width by derivedStateOf { rightBlock?.width?.value ?: 60.dp }

    Row(
        modifier = Modifier.fillMaxSize()
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
    Row(
        modifier = Modifier.fillMaxSize()
    ){
        Text(
            stringResource(R.string.in_list),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
                .fillMaxHeight()
                .padding(4.dp)
                .background(EmptySpace)
        ){}

        Text(
            stringResource(R.string.edit),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 14.sp),
            modifier = Modifier.padding(4.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
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