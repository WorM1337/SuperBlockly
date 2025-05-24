package com.unewexp.superblockly

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

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
fun toHomeBtn(navController: NavHostController){
    Button(
        { navController.navigate(Routes.Home.route) },
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
fun rememberDragState() = remember { DragState() }