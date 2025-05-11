package com.unewexp.superblockly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapplicatioin.blocks.VariableBlock
import com.example.myfirstapplicatioin.viewBlocks.DrawConnection
import com.example.myfirstapplicatioin.viewBlocks.ViewVariableBlock
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme

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
        .padding(0.dp, 2.dp, 0.dp, 0.dp)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SuperBlocklyTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.Home.route) {

                        composable(Routes.Home.route) { Home(navController) }
                        composable(Routes.CreateProject.route) { CreateNewProject(navController)  }
                        composable(Routes.MyProjects.route) { MyProjects(navController)  }
                        composable(Routes.About.route) { About(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun Home(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.Center
    ){
        Column {
            Button(
                { navController.navigate(Routes.CreateProject.route) },
                Modifiers.homeBtnModifier
            ){
                Text(stringResource(R.string.create_new_project))
            }
            Button(
                { navController.navigate(Routes.MyProjects.route) },
                Modifiers.homeBtnModifier
            ){
                Text(stringResource(R.string.my_projects))
            }
            Button(
                { navController.navigate(Routes.About.route) },
                Modifiers.homeBtnModifier
            ){
                Text(stringResource(R.string.about))
            }
        }
    }
}

@Composable
fun CreateNewProject(navController: NavHostController){
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        toHomeBtn(navController)
    }
    Box(
        contentAlignment = Alignment.Center
    ){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    val variableBlock = VariableBlock("value")
                    val view = ViewVariableBlock(variableBlock, 100.dp, 100.dp)
                    view.startView(DrawConnection) // нужно еще во viewer закинуть все переменные
                }
            }
        )
    }
}

@Composable
fun MyProjects(navController: NavHostController){
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        toHomeBtn(navController)
    }
    Box(
        contentAlignment = Alignment.Center
    ){

    }
}

@Composable
fun About(navController: NavHostController){
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        toHomeBtn(navController)
    }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Text(
            stringResource(R.string.about),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            modifier = Modifier.padding(2.dp)
        )
    }
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .padding(5.dp, 0.dp, 0.dp, 0.dp)
    ){
        Column{
            Text(stringResource(R.string.about_title), style = MaterialTheme.typography.titleLarge)
            Text(
                stringResource(R.string.about_body1),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
                stringResource(R.string.about_body2),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
            Text(
                stringResource(R.string.about_body3),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 0.dp, 0.dp)
            )
        }
    }
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