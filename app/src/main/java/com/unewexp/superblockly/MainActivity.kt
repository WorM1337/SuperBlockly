@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.unewexp.superblockly.blocks.logic.IfBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.debug.ConsolePanel
import com.unewexp.superblockly.ui.theme.DrawerColor
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import kotlinx.coroutines.launch
import kotlin.math.roundToInt




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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateNewProject(
    navController: NavHostController,
    viewModel: DraggableViewModel = viewModel()
){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val globalOffset = remember { mutableStateOf(Offset.Zero) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            Column(modifier = Modifier
                .background(DrawerColor)
            ){
                Button(onClick = { scope.launch { drawerState.close() } }) {
                    Text("Закрыть")
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ){
                    item{
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        PrintBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(100.dp)
                                    )
                                )
                            }
                        ){
                            PrintBlockCard()
                        }
                    }
                    item{
                        Text("Математика", color = Color.White)
                    }
                    item {
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        IntLiteralBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                            }
                        ){
                            IntLiteralBlockCard()
                        }
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        OperandBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                            }
                        ){
                            OperandBlockCard()
                        }
                    }
                    item { Text("Переменные", color = Color.White) }
                    item {
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        SetValueVariableBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                            }
                        ){
                            SetValueVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        VariableDeclarationBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                            }
                        ){
                            DeclarationVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        VariableReferenceBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(150.dp)
                                    )
                                )
                            }
                        ){
                            ReferenceVariableCard()
                        }
                    }
                    item {
                        Text("Логика", color = Color.White)
                    }
                    item {
                        ListItem(
                            { offset ->
                                viewModel.addBlock(
                                    DraggableBlock(
                                        IfBlock(),
                                        mutableStateOf(offset.x - globalOffset.value.x),
                                        mutableStateOf(offset.y - globalOffset.value.y),
                                        width = mutableStateOf(100.dp)
                                    )
                                )
                            }
                        ){
                            IfBlockCard()
                        }
                    }
                }
            }
        }
    ) {
        Canvas(
            { scope.launch { drawerState.open() } },
            { toHomeBtn(navController) },
            {newOffset -> globalOffset.value = newOffset }
        )
    }
}

@Composable
fun ListItem(
    onDoubleClick: (offset: Offset) -> Unit,
    content: @Composable () -> Unit
){

    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                dragStartPosition = coordinates.positionInRoot()
            }
            .pointerInput(Unit){
                detectTapGestures(
                    onDoubleTap = {
                        onDoubleClick(dragStartPosition)
                    }
                )
            }
    ) {
        content()
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