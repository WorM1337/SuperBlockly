@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
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
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.myfirstapplicatioin.viewBlocks.ViewIntLiteralBlock
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
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val globalOffset = remember { mutableStateOf(Offset.Zero) }
    val canBeDraggable = remember { mutableStateOf(false) }

    var currentDragPosition by remember { mutableStateOf(Offset.Zero) }
    var dragStartPosition1 by remember { mutableStateOf(Offset.Zero) }
    var dragStartPosition2 by remember { mutableStateOf(Offset.Zero) }
    var dragStartPosition3 by remember { mutableStateOf(Offset.Zero) }
    var dragStartPosition4 by remember { mutableStateOf(Offset.Zero) }

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
                    ) {
                        Text("Математика", color = Color.White)
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned{ coordinates ->
                                    dragStartPosition1 = coordinates.positionInRoot()
                                }
                                .pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            canBeDraggable.value = true
                                        }
                                    )
                                }
                                .pointerInteropFilter{
                                    if (it.action == MotionEvent.ACTION_UP) {
                                        canBeDraggable.value = false
                                    }

                                    true
                                }
                                .pointerInput(Unit){
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            ghostContent = { IntLiteralBlockCard() }
                                            ghostPosition = dragStartPosition1
                                            currentDragPosition = dragStartPosition1
                                            ghostVisible = true
                                            canBeDraggable.value = true
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            ghostPosition += dragAmount
                                            currentDragPosition += dragAmount
                                        },
                                        onDragEnd = {
                                            val newBlock = IntLiteralBlock()
                                            viewModel.addBlock(
                                                DraggableBlock(
                                                    newBlock.id.toString(),
                                                    newBlock,
                                                    mutableStateOf(currentDragPosition.x - globalOffset.value.x),
                                                    mutableStateOf(currentDragPosition.y - globalOffset.value.y),
                                                    width = 100
                                                )
                                            )
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        },
                                        onDragCancel = {
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            IntLiteralBlockCard()
                        }
                        Text("Переменные", color = Color.White)
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned{ coordinates ->
                                    dragStartPosition2 = coordinates.positionInRoot()
                                }
                                .pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            canBeDraggable.value = true
                                        }
                                    )
                                }
                                .pointerInteropFilter{
                                    if (it.action == MotionEvent.ACTION_UP) {
                                        canBeDraggable.value = false
                                    }

                                    true
                                }
                                .pointerInput(Unit){
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            ghostContent = { SetValueVariableCard() }
                                            ghostPosition = dragStartPosition2
                                            currentDragPosition = dragStartPosition2
                                            ghostVisible = true
                                            canBeDraggable.value = true
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            ghostPosition += dragAmount
                                            currentDragPosition += dragAmount
                                        },
                                        onDragEnd = {
                                            val newBlock = SetValueVariableBlock()
                                            viewModel.addBlock(
                                                DraggableBlock(
                                                    newBlock.id.toString(),
                                                    newBlock,
                                                    mutableStateOf(currentDragPosition.x - globalOffset.value.x),
                                                        mutableStateOf(currentDragPosition.y - globalOffset.value.y),
                                                    width = 100
                                                )
                                            )
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        },
                                        onDragCancel = {
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            SetValueVariableCard()
                        }
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned{ coordinates ->
                                    dragStartPosition3 = coordinates.positionInRoot()
                                }
                                .pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            canBeDraggable.value = true
                                        }
                                    )
                                }
                                .pointerInteropFilter{
                                    if (it.action == MotionEvent.ACTION_UP) {
                                        canBeDraggable.value = false
                                    }

                                    true
                                }
                                .pointerInput(Unit){
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            ghostContent = { DeclarationVariableCard() }
                                            ghostPosition = dragStartPosition3
                                            currentDragPosition = dragStartPosition3
                                            ghostVisible = true
                                            canBeDraggable.value = true
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            ghostPosition += dragAmount
                                            currentDragPosition += dragAmount
                                        },
                                        onDragEnd = {
                                            val newBlock = VariableDeclarationBlock()
                                            viewModel.addBlock(
                                                DraggableBlock(
                                                    newBlock.id.toString(),
                                                    newBlock,
                                                    mutableStateOf(currentDragPosition.x - globalOffset.value.x),
                                                    mutableStateOf(currentDragPosition.y - globalOffset.value.y),
                                                    width = 100
                                                )
                                            )
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        },
                                        onDragCancel = {
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            DeclarationVariableCard()
                        }
                        Box(
                            modifier = Modifier
                                .onGloballyPositioned{ coordinates ->
                                    dragStartPosition4 = coordinates.positionInRoot()
                                }
                                .pointerInput(Unit){
                                    detectTapGestures(
                                        onLongPress = {
                                            canBeDraggable.value = true
                                        }
                                    )
                                }
                                .pointerInteropFilter{
                                    if (it.action == MotionEvent.ACTION_UP) {
                                        canBeDraggable.value = false
                                    }

                                    true
                                }
                                .pointerInput(Unit){
                                    detectDragGestures(
                                        onDragStart = { offset ->
                                            ghostContent = { ReferenceVariableCard() }
                                            ghostPosition = dragStartPosition4
                                            currentDragPosition = dragStartPosition4
                                            ghostVisible = true
                                            canBeDraggable.value = true
                                        },
                                        onDrag = { change, dragAmount ->
                                            change.consume()
                                            ghostPosition += dragAmount
                                            currentDragPosition += dragAmount
                                        },
                                        onDragEnd = {
                                            val newBlock = VariableReferenceBlock()
                                            viewModel.addBlock(
                                                DraggableBlock(
                                                    newBlock.id.toString(),
                                                    newBlock,
                                                    mutableStateOf(currentDragPosition.x - globalOffset.value.x),
                                                    mutableStateOf(currentDragPosition.y - globalOffset.value.y),
                                                    width = 100
                                                )
                                            )
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        },
                                        onDragCancel = {
                                            ghostVisible = false
                                            canBeDraggable.value = false
                                            currentDragPosition = Offset.Zero
                                        }
                                    )
                                }
                        ) {
                            ReferenceVariableCard()
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

                val startW = LocalConfiguration.current.screenWidthDp
                val startH = LocalConfiguration.current.screenHeightDp
                var currentScale = remember{ mutableStateOf(1f) }

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
                        DraggableBase(
                            content = {
                                IfItIsThisBlock(it, viewModel)
                                Box(
                                    modifier = Modifier
                                        .size(15.dp, 15.dp)
                                        .offset(it.outputConnectionView!!.positionX - 7.dp, it.outputConnectionView!!.positionY - 7.dp)
                                        .background(Color.Red)
                                )

                                it.inputConnectionViews.forEach{
                                    Box(
                                        modifier = Modifier
                                            .size(15.dp, 15.dp)
                                            .offset(it.positionX - 7.dp, it.positionY - 7.dp)
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
                                ConnectorManager.tryConnectDrag(it, viewModel)
                            }
                        )
                    }
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
        BlockType.VOID_BLOCK -> TODO()
        BlockType.PRINT_BLOCK -> TODO()
    }
}