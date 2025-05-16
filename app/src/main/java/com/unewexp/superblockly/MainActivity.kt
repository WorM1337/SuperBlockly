@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapplicatioin.viewBlocks.ViewBlock
import com.example.myfirstapplicatioin.viewBlocks.ViewIntLiteralBlock
import com.unewexp.superblockly.ui.theme.DrawerColor
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme
import com.unewexp.superblockly.viewBlocks.ViewSetValueVariableBlock
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

sealed class Routes(val route: String) {

    object Home : Routes("home")
    object CreateProject : Routes("create project")
    object MyProjects : Routes("my projects")
    object About : Routes("about")
}

object BlockFactory {
    fun createIntBlock(x: Dp, y: Dp): ViewIntLiteralBlock {
        return ViewIntLiteralBlock(x, y)
    }

    fun createVariableBlock(x: Dp, y: Dp): ViewSetValueVariableBlock {
        return ViewSetValueVariableBlock(x, y)
    }
}

class CanvasState {
    private val _blocks = mutableStateListOf<ViewBlock>()
    val blocks: List<ViewBlock> get() = _blocks

    fun addBlock(block: ViewBlock) {
        _blocks.add(block)
    }

    fun removeBlock(block: ViewBlock) {
        _blocks.remove(block)
    }
}

object Modifiers{
    val homeBtnModifier: Modifier = Modifier
        .width(250.dp)
        .padding(5.dp)
    val toHomeBtnMod: Modifier = Modifier
        .padding(0.dp, 0.dp, 0.dp, 0.dp)
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val transferAction = "action"
    val blockTypeTransferData = "block_type"
    val blockViewTransferData = "block_view"

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier
                    .background(DrawerColor)
            ){
                Column(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ) {
                    Button(onClick = { scope.launch { drawerState.close() } }) {
                        Text("Закрыть")
                    }
                    Column(
                        modifier = Modifier
                            .padding(10.dp, 0.dp)
                            /*
                            .dragAndDropSource(drawDragDecoration = {
                                // UI перетаскиваемого элемента
                            }){
                                startTransfer(
                                    DragAndDropTransferData(
                                        clipData = ClipData.newIntent(
                                            "block_type",
                                            Intent(transferAction).apply {
                                                putExtra(
                                                    blockTypeTransferData,

                                                )
                                            }
                                        )
                                    )
                                )
                            }
                        */
                    ) {
                        Text("Математика", color = Color.White)
                        BlockCard(ViewIntLiteralBlock(100.dp, 100.dp))
                    }
                }
            }
        }
    ) {
        val zoomFactor = 0.7f
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row {
                    Box(contentAlignment = Alignment.TopStart) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.List, null)
                        }
                    }
                    Box(contentAlignment = Alignment.TopEnd) {
                        toHomeBtn(navController)
                    }
                }
            },
            content = { paddingValues ->
                val offset = remember { mutableStateOf(Offset.Zero) }
                val scale = remember { mutableFloatStateOf(1f) }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Color.LightGray)

                        .transformable(
                            state = rememberTransformableState { zoomChange, offsetChange, _ ->
                                scale.value *= 1f + (zoomChange - 1f) * zoomFactor
                                offset.value += offsetChange
                            }
                        )
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            translationX = offset.value.x,
                            translationY = offset.value.y
                        )
                ) {
                    val view = ViewSetValueVariableBlock(100.dp, 100.dp)
                    view.render()
                    val view2 = ViewIntLiteralBlock(400.dp, 100.dp)
                    view2.render()
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
