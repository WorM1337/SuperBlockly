@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.unewexp.superblockly.blocks.logic.IfBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.ui.theme.DrawerColor
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme
import com.unewexp.superblockly.viewBlocks.DeclarationVariableView
import com.unewexp.superblockly.viewBlocks.DraggableBase
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
import kotlinx.coroutines.launch
import kotlin.math.roundToInt




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
    val density = LocalDensity.current

    fun dpToPx(dp: Dp): Float {
        val pxValue = with(density) {dp.toPx()}  // Упрощённый расчёт

        return pxValue.toFloat()
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val globalOffset = remember { mutableStateOf(Offset.Zero) }
    val canBeDraggable = remember { mutableStateOf(false) }

    var ghostVisible by remember { mutableStateOf(false) }
    var ghostPosition by remember { mutableStateOf(Offset.Zero) }
    var ghostContent by remember { mutableStateOf<@Composable () -> Unit>({ }) }

    val blocks by viewModel.blocks.collectAsState()

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
                                    val newBlock = IntLiteralBlock()
                                    viewModel.addBlock(
                                        DraggableBlock(
                                            newBlock.id.toString(),
                                            newBlock,
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
                                val newBlock = SetValueVariableBlock()
                                viewModel.addBlock(
                                    DraggableBlock(
                                        newBlock.id.toString(),
                                        newBlock,
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
                                val newBlock = VariableDeclarationBlock()
                                viewModel.addBlock(
                                    DraggableBlock(
                                        newBlock.id.toString(),
                                        newBlock,
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
                                val newBlock = VariableReferenceBlock()
                                viewModel.addBlock(
                                    DraggableBlock(
                                        newBlock.id.toString(),
                                        newBlock,
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
                                val newBlock = IfBlock()
                                viewModel.addBlock(
                                    DraggableBlock(
                                        newBlock.id.toString(),
                                        newBlock,
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
        val zoomFactor = 0.7f
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.White)
                ) {
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
                val scale = remember { mutableFloatStateOf(1f) }
                val currentScale = remember{ mutableStateOf(1f) }

                Box(
                    modifier = Modifier
                        .width(4000.dp)
                        .height(2000.dp)
                        .padding(paddingValues)
                        .background(Color.LightGray)

                        .transformable(
                            state = rememberTransformableState { zoomChange, offsetChange, _ ->
                                scale.value *= 1f + (zoomChange - 1f) * zoomFactor
                                currentScale.value += scale.floatValue - 1
                                globalOffset.value += offsetChange
                            }
                        )
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            translationX = globalOffset.value.x,
                            translationY = globalOffset.value.y
                        )
                ) {
                    blocks.forEach {
                        Log.i("render", "${it.block.blockType} with id: " + it.id)
                        DraggableBase(
                            content = {
                                IfItIsThisBlock(it, viewModel)
                                Box(
                                    modifier = Modifier
                                        .size(15.dp)
                                        .offset(it.outputConnectionView!!.positionX, it.outputConnectionView!!.positionY)
                                        .background(Color.Red)
                                )

                                it.inputConnectionViews.forEach{
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp)
                                            .offset(it.positionX, it.positionY)
                                            .background(Color.Red)
                                    )
                                }

                            },
                            it,
                            onPositionChanged = { offsetX, offsetY ->
                                viewModel.updateBlockPosition(it.id, offsetX, offsetY)
                            },
                            onDoubleTap = {
                                viewModel.removeBlock(it.id)
                            },
                            onDragEnd = {
                                ConnectorManager.tryConnectDrag(it, viewModel, density)
                            }
                        )

                        Box(modifier = Modifier
                            .size(15.dp)
                            .offset {
                                IntOffset(
                                    (it.x.value + dpToPx(it.outputConnectionView!!.positionX)).roundToInt(),
                                    (it.y.value + dpToPx(it.outputConnectionView!!.positionY)).roundToInt()
                                )
                            }
                            .background(Color.Green)

                        )
                        it.inputConnectionViews.forEach { connectionView ->
                            Box(modifier = Modifier
                                .size(15.dp)
                                .offset {
                                    IntOffset(
                                        (it.x.value + dpToPx(connectionView.positionX)).roundToInt(),
                                        (it.y.value + dpToPx(connectionView.positionY)).roundToInt()
                                    )
                                }
                                .background(Color.Magenta)
                            )
                        }
                    }
                }
            }
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

@Composable
fun IfItIsThisBlock(block: DraggableBlock, viewModel: DraggableViewModel = viewModel()){
    val blockType = block.block.blockType
    when(blockType){
        BlockType.OPERAND -> TODO()
        BlockType.SET_VARIABLE_VALUE -> SetValueVariableView({ newValue ->
            viewModel.updateValue(block.id, newValue)
        })
        BlockType.START -> TODO()
        BlockType.VARIABLE_DECLARATION -> DeclarationVariableView(
            { newValue ->
                viewModel.updateValue(block.id, newValue)
            }
        )
        BlockType.INT_LITERAL -> IntLiteralView({ newValue ->
            viewModel.updateValue(block.id, newValue)
        })
        BlockType.STRING_LITERAL -> TODO()
        BlockType.BOOLEAN_LITERAL -> TODO()
        BlockType.VARIABLE_REFERENCE -> VariableReferenceView(
            { newValue ->
                viewModel.updateValue(block.id, newValue)
            }
        )
        BlockType.STRING_CONCAT -> TODO()
        BlockType.STRING_APPEND -> TODO()
        BlockType.PRINT_BLOCK -> TODO()
        BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
        BlockType.COMPARE_NUMBERS_BLOCK -> TODO()
        BlockType.BOOLEAN_LOGIC_BLOCK -> TODO()
        BlockType.NOT_BLOCK -> TODO()
        BlockType.IF_BLOCK -> IfBlockView(block.height.value)
        BlockType.ELSE_BLOCK -> TODO()
        BlockType.IF_ELSE_BLOCK -> TODO()
        BlockType.REPEAT_N_TIMES -> TODO()
        BlockType.WHILE_BLOCK -> TODO()
        BlockType.FOR_BLOCK -> TODO()
        BlockType.FOR_ELEMENT_IN_LIST -> TODO()
        BlockType.FIXED_VALUE_AND_SIZE_LIST -> TODO()
        BlockType.GET_VALUE_BY_INDEX -> TODO()
        BlockType.REMOVE_VALUE_BY_INDEX -> TODO()
        BlockType.ADD_VALUE_BY_INDEX -> TODO()
        BlockType.GET_LIST_SIZE -> TODO()
    }
}