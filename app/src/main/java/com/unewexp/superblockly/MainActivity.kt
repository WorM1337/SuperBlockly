@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import com.unewexp.superblockly.ui.theme.DrawerColor
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme
import com.unewexp.superblockly.viewBlocks.DraggableBlock
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
    val canBeDraggable = remember { mutableStateOf(false) }

    var ghostVisible by remember { mutableStateOf(false) }
    var ghostPosition by remember { mutableStateOf(Offset.Zero) }
    var ghostContent by remember { mutableStateOf<@Composable () -> Unit>({ }) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            Box(modifier = Modifier
                .background(DrawerColor)
            ){
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ) {
                    item{
                        Button(onClick = { scope.launch { drawerState.close() } }) {
                            Text("Закрыть")
                        }
                    }
                    item{
                        ListItem(
                            {
                                canBeDraggable.value = true
                            },
                            {
                                canBeDraggable.value = false
                            },
                            { dragStartPosition ->
                                ghostContent = { PrintBlockCard() }
                                ghostPosition = dragStartPosition
                                ghostVisible = true
                                canBeDraggable.value = true
                            },
                            { dragAmount ->
                                ghostPosition += dragAmount
                            },
                            {
                                viewModel.addBlock(
                                    DraggableBlock(
                                        PrintBlock(),
                                        mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                        mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                        width = mutableStateOf(100.dp)
                                    )
                                )
                                ghostVisible = false
                                canBeDraggable.value = false
                            },
                            {
                                ghostVisible = false
                                canBeDraggable.value = false
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
                                {
                                    canBeDraggable.value = true
                                },
                                {
                                    canBeDraggable.value = false
                                },
                                { dragStartPosition ->
                                    ghostContent = { IntLiteralBlockCard() }
                                    ghostPosition = dragStartPosition
                                    ghostVisible = true
                                    canBeDraggable.value = true
                                },
                                { dragAmount ->
                                    ghostPosition += dragAmount
                                },
                                {
                                    viewModel.addBlock(
                                        DraggableBlock(
                                            IntLiteralBlock(),
                                            mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                            mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                            width = mutableStateOf(100.dp)
                                        )
                                    )
                                    ghostVisible = false
                                    canBeDraggable.value = false
                                },
                                {
                                    ghostVisible = false
                                    canBeDraggable.value = false
                                }
                            ){
                                IntLiteralBlockCard()
                            }
                        }
                        item { Text("Переменные", color = Color.White) }
                    item {
                        ListItem(
                            {
                                canBeDraggable.value = true
                            },
                            {
                                canBeDraggable.value = false
                            },
                            { dragStartPosition ->
                                ghostContent = { SetValueVariableCard() }
                                ghostPosition = dragStartPosition
                                ghostVisible = true
                                canBeDraggable.value = true
                            },
                            { dragAmount ->
                                ghostPosition += dragAmount
                            },
                            {
                                viewModel.addBlock(
                                    DraggableBlock(
                                        SetValueVariableBlock(),
                                        mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                        mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                                ghostVisible = false
                                canBeDraggable.value = false
                            },
                            {
                                ghostVisible = false
                                canBeDraggable.value = false
                            }
                        ){
                            SetValueVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            {
                                canBeDraggable.value = true
                            },
                            {
                                canBeDraggable.value = false
                            },
                            { dragStartPosition ->
                                ghostContent = { DeclarationVariableCard() }
                                ghostPosition = dragStartPosition
                                ghostVisible = true
                                canBeDraggable.value = true
                            },
                            { dragAmount ->
                                ghostPosition += dragAmount
                            },
                            {
                                viewModel.addBlock(
                                    DraggableBlock(
                                        VariableDeclarationBlock(),
                                        mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                        mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                        width = mutableStateOf(200.dp)
                                    )
                                )
                                ghostVisible = false
                                canBeDraggable.value = false
                            },
                            {
                                ghostVisible = false
                                canBeDraggable.value = false
                            }
                        ){
                            DeclarationVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            {
                                canBeDraggable.value = true
                            },
                            {
                                canBeDraggable.value = false
                            },
                            { dragStartPosition ->
                                ghostContent = { ReferenceVariableCard() }
                                ghostPosition = dragStartPosition
                                ghostVisible = true
                                canBeDraggable.value = true
                            },
                            { dragAmount ->
                                ghostPosition += dragAmount
                            },
                            {
                                viewModel.addBlock(
                                    DraggableBlock(
                                        VariableReferenceBlock(),
                                        mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                        mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                        width = mutableStateOf(100.dp)
                                    )
                                )
                                ghostVisible = false
                                canBeDraggable.value = false
                            },
                            {
                                ghostVisible = false
                                canBeDraggable.value = false
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
                            {
                                canBeDraggable.value = true
                            },
                            {
                                canBeDraggable.value = false
                            },
                            { dragStartPosition ->
                                ghostContent = { IfBlockCard() }
                                ghostPosition = dragStartPosition
                                ghostVisible = true
                                canBeDraggable.value = true
                            },
                            { dragAmount ->
                                ghostPosition += dragAmount
                            },
                            {
                                viewModel.addBlock(
                                    DraggableBlock(
                                        IfBlock(),
                                        mutableStateOf(ghostPosition.x - globalOffset.value.x),
                                        mutableStateOf(ghostPosition.y - globalOffset.value.y),
                                        width = mutableStateOf(100.dp)
                                    )
                                )
                                ghostVisible = false
                                canBeDraggable.value = false
                            },
                            {
                                ghostVisible = false
                                canBeDraggable.value = false
                            }
                        ){
                            IfBlockCard()
                        }
                    }
                }
                if (ghostVisible) {
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(ghostPosition.x.roundToInt(), ghostPosition.y.roundToInt()) }
                            .pointerInput(Unit) { }
                    ) {
                        ghostContent()
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListItem(
    onLongPress: () -> Unit,
    pointerInteropFilter: () -> Unit,
    onDragStart: (dragStartPosition: Offset) -> Unit,
    onDrag: (dragAmount: Offset) -> Unit,
    onDragEnd: () -> Unit,
    onDragCancel: () -> Unit,
    content: @Composable () -> Unit
){

    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                dragStartPosition = coordinates.positionInRoot()
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress() }
                )
            }
            .pointerInteropFilter {
                if (it.action == MotionEvent.ACTION_UP) {
                    pointerInteropFilter()
                }

                true
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onDragStart(dragStartPosition)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragCancel() }
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

/*
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DraggableListItem(
    viewModel: DraggableViewModel,
    createBlock: () -> Block,
    defaultWidth: Dp,
    ghostContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val dragState = rememberDragState()
    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                dragStartPosition = coordinates.positionInRoot()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        dragState.onDragStart(dragStartPosition)
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragState.onDrag(dragAmount)
                    },
                    onDragEnd = {
                        val newBlock = createBlock()
                        viewModel.addBlock(
                            DraggableBlock(
                                newBlock.id.toString(),
                                newBlock,
                                x = mutableStateOf(dragState.ghostPosition.x - dragState.globalOffset.x),
                                y = mutableStateOf(dragState.ghostPosition.y - dragState.globalOffset.y),
                                width = mutableStateOf(defaultWidth)
                            )
                        )
                        dragState.onDragEnd()
                    },
                    onDragCancel = { dragState.onDragCancel() }
                )
            }
    ) {
        content()
    }

    if (dragState.isGhostVisible) {
        Box(
            modifier = Modifier
                .offset { dragState.ghostPosition.round() }
                .pointerInput(Unit) { }
                .zIndex(1f) // Поверх других элементов
        ) {
            ghostContent()
        }
    }
}

 */

/*
@Composable
fun CreateNewProject(
    navController: NavHostController,
    viewModel: DraggableViewModel = viewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val dragState = rememberDragState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(Modifier.background(DrawerColor)) {
                LazyColumn(Modifier.padding(5.dp)) {
                    item { Button(onClick = { scope.launch { drawerState.close() } }) { Text("Закрыть") } }

                    // Пример элемента
                    item {
                        DraggableListItem(
                            viewModel = viewModel,
                            createBlock = { IntLiteralBlock() },
                            defaultWidth = 100.dp,
                            ghostContent = { IntLiteralBlockCard(Modifier.alpha(0.7f)) }
                        ) {
                            IntLiteralBlockCard()
                        }
                    }

                    // Другие элементы...
                }
            }
        }
    ) {
        Canvas(
            onOpenDrawer = { scope.launch { drawerState.open() } },
            onHomeClick = { toHomeBtn(navController) },
            onGlobalOffsetChange = { dragState.globalOffset = it }
        )
    }
}
 */