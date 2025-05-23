package com.unewexp.superblockly

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
