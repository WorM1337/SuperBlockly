package com.unewexp.superblockly.viewBlocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.unewexp.superblockly.R

@Composable
fun IntLiteralView(
    onNameChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf(TextFieldValue("0")) }

    TextField(
        value = value,
        onValueChange = {
            if (it.text.isEmpty()) {
                value = TextFieldValue("0")
            } else if (it.text.matches(Regex("^\\d*\$"))) {
                value = it
            }
            onNameChanged(value.text)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        placeholder = { Text("0", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
        singleLine = true,
        modifier = Modifier.fillMaxSize(),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun IntLiteralViewForCard(){

    var value by remember { mutableStateOf(TextFieldValue("0")) }

    TextField(
        enabled = false,
        value = value,
        onValueChange = {
            if(it.text.isEmpty()){
                value = TextFieldValue("0")
            }else{
                value = it
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text("Num") },
        singleLine = true,
        modifier = Modifier
            .padding(horizontal = 4.dp),
        textStyle = MaterialTheme.typography.bodySmall
    )
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

    var name by remember { mutableStateOf(TextFieldValue("")) }

    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            "Set",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextField(
            enabled = false,
            value = name,
            onValueChange = {
                name = it
            },
            placeholder = { Text("Var") },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            textStyle = MaterialTheme.typography.bodySmall
        )
        Text(
            "to",
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
                .padding(horizontal = 12.dp, vertical = 18.dp),
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
    var name by remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        enabled = false,
        value = name,
        onValueChange = {},
        placeholder = { Text("Var") },
        singleLine = true,
        modifier = Modifier
            .padding(horizontal = 4.dp),
        textStyle = MaterialTheme.typography.bodySmall
    )
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

    var name by remember { mutableStateOf(TextFieldValue("")) }

    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            "Init",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextField(
            enabled = false,
            value = name,
            onValueChange = {},
            placeholder = { Text("Var") },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            textStyle = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun IfBlockView() {
    val textHeight = 24.dp
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
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    "If",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White, fontSize = 20.sp),
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
    Column {
        Box{
            Text("If")
        }
    }
}

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