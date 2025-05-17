package com.unewexp.superblockly.viewBlocks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun IntLiteralView(
    onNameChanged: (String) -> Unit
    ){

    var value by remember { mutableStateOf(TextFieldValue("0")) }

    TextField(
        value = value,
        onValueChange = {
            if(it.text.isEmpty()){
                value = TextFieldValue("0")
            }else{
                value = it
            }
            onNameChanged(value.text)
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
){

    var name by remember { mutableStateOf(TextFieldValue("")) }

    Row(verticalAlignment = Alignment.CenterVertically){
        Text(
            "Set",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 4.dp)
        )
        TextField(
            value = name,
            onValueChange = {
                name = it
                onNameChanged(name.text)
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