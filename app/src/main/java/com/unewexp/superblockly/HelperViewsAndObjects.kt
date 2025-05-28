package com.unewexp.superblockly

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.ui.theme.BooleanColor
import com.unewexp.superblockly.ui.theme.ListColor
import com.unewexp.superblockly.ui.theme.LoopColor
import com.unewexp.superblockly.ui.theme.MathColor
import com.unewexp.superblockly.ui.theme.PrintColor
import com.unewexp.superblockly.ui.theme.StartColor
import com.unewexp.superblockly.ui.theme.VariablesColor

sealed class Routes(val route: String) {

    object Home : Routes("home")
    object CreateProject : Routes("create project")
    object MyProjects : Routes("my projects")
    object About : Routes("about")
}


object Modifiers{
    val homeBtnModifier: Modifier = Modifier
        .width(250.dp)
        .padding(5.dp)
    val toHomeBtnMod: Modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 0.dp)
}

@Composable
fun toHomeBtn(
    navController: NavHostController,
    onClick: () -> Unit = {}
){
    Button(
        {
            navController.navigate(Routes.Home.route)
            onClick()
        },
        Modifiers.toHomeBtnMod,
        shape = RectangleShape,
    ) {
        Text("Домой")
    }
}


class DragState {
    var isGhostVisible by mutableStateOf(false)
    var ghostPosition by mutableStateOf(Offset.Zero)
    var globalOffset by mutableStateOf(Offset.Zero)

    fun onDragStart(dragStartPosition: Offset) {
        ghostPosition = dragStartPosition
        isGhostVisible = true
    }

    fun onDrag(dragAmount: Offset) {
        ghostPosition += dragAmount
    }

    fun onDragEnd() {
        isGhostVisible = false
    }

    fun onDragCancel() {
        isGhostVisible = false
    }
}

@Composable
fun getColorByBlockType(type: BlockType): Color {
    var color: Color = Color(0xFFE0E0E0)
    when(type){
        BlockType.SET_VARIABLE_VALUE -> color = VariablesColor
        BlockType.START -> color = StartColor
        BlockType.INT_LITERAL -> color = MathColor
        BlockType.STRING_LITERAL -> TODO()
        BlockType.BOOLEAN_LITERAL -> color = BooleanColor
        BlockType.OPERAND -> color = MathColor
        BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
        BlockType.VARIABLE_DECLARATION -> color = VariablesColor
        BlockType.VARIABLE_REFERENCE -> color = VariablesColor
        BlockType.STRING_CONCAT -> TODO()
        BlockType.STRING_APPEND -> TODO()
        BlockType.PRINT_BLOCK -> color = PrintColor
        BlockType.COMPARE_NUMBERS_BLOCK -> color = BooleanColor
        BlockType.BOOLEAN_LOGIC_BLOCK -> color = BooleanColor
        BlockType.NOT_BLOCK -> TODO()
        BlockType.IF_BLOCK -> color = BooleanColor
        BlockType.ELSE_BLOCK -> color = BooleanColor
        BlockType.IF_ELSE_BLOCK -> color = BooleanColor
        BlockType.REPEAT_N_TIMES -> color = LoopColor
        BlockType.WHILE_BLOCK -> color = LoopColor
        BlockType.FOR_BLOCK -> color = LoopColor
        BlockType.FOR_ELEMENT_IN_LIST -> color = LoopColor
        BlockType.FIXED_VALUE_AND_SIZE_LIST -> color = ListColor
        BlockType.GET_VALUE_BY_INDEX -> TODO()
        BlockType.REMOVE_VALUE_BY_INDEX -> TODO()
        BlockType.ADD_VALUE_BY_INDEX -> TODO()
        BlockType.GET_LIST_SIZE -> TODO()
    }
    return color
}

@Composable
fun Spinner(
    size: Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .size(size)
            .clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }
}