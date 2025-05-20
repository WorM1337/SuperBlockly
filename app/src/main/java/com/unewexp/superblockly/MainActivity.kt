@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.content.ClipData
import android.content.ClipDescription
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.viewBlocks.ViewBlock
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
import com.unewexp.superblockly.viewBlocks.IntLiteralViewForCard
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.SetValueVariableViewForCard
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
import com.unewexp.superblockly.viewBlocks.ViewSetValueVariableBlock
import kotlinx.coroutines.launch

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

@Composable
fun CreateNewProject(
    navController: NavHostController,
    viewModel: DraggableViewModel = viewModel()
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val blocks by viewModel.blocks.collectAsState()

    val transferAction = "action"
    val blockTypeTransferData = "block_type"
    val blockViewTransferData = "block_view"

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
                                .dragAndDropSource(
                                    drawDragDecoration = {
                                        // Потом тут будет просто картинка, и в остальных перетаскиваемых блоках тоже
                                        drawRect(
                                            color = Color.Gray.copy(alpha = 0.7f),
                                            size = Size(200f, 40f)
                                        )
                                    }
                                ) {
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newPlainText(
                                                "block_type",
                                                "IntLiteralBlock"
                                            )
                                        )
                                    )
                                }
                        ) {
                            IntLiteralBlockCard()
                        }
                        Text("Переменные", color = Color.White)
                        Box(
                            modifier = Modifier
                                .dragAndDropSource(
                                    drawDragDecoration = {
                                        // Потом тут будет просто картинка, и в остальных перетаскиваемых блоках тоже
                                        drawRect(
                                            color = Color.Gray.copy(alpha = 0.7f),
                                            size = Size(20f, 40f)
                                        )
                                    }
                                ) {
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newPlainText(
                                                "block_type",
                                                "SetValueVariableBlock"
                                            )
                                        )
                                    )
                                }
                        ) {
                            SetValueVariableCard()
                        }
                        Box(
                            modifier = Modifier
                                .dragAndDropSource(
                                    drawDragDecoration = {
                                        // Потом тут будет просто картинка, и в остальных перетаскиваемых блоках тоже
                                        drawRect(
                                            color = Color.Gray.copy(alpha = 0.7f),
                                            size = Size(20f, 40f)
                                        )
                                    }
                                ) {
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newPlainText(
                                                "block_type",
                                                "VariableDeclarationBlock"
                                            )
                                        )
                                    )
                                }
                        ) {
                            DeclarationVariableCard()
                        }
                        Box(
                            modifier = Modifier
                                .dragAndDropSource(
                                    drawDragDecoration = {
                                        // Потом тут будет просто картинка, и в остальных перетаскиваемых блоках тоже
                                        drawRect(
                                            color = Color.Gray.copy(alpha = 0.7f),
                                            size = Size(20f, 40f)
                                        )
                                    }
                                ) {
                                    startTransfer(
                                        DragAndDropTransferData(
                                            clipData = ClipData.newPlainText(
                                                "block_type",
                                                "VariableReferenceBlock"
                                            )
                                        )
                                    )
                                }
                        ) {
                            ReferenceVariableCard()
                        }
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
                                Log.i("ZOOM", zoomChange.toString())
                                scale.value *= 1f + (zoomChange - 1f) * zoomFactor
                                currentScale.value += scale.floatValue - 1
                                offset.value += offsetChange
                            }
                        )
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            translationX = offset.value.x,
                            translationY = offset.value.y
                        )
                        .dragAndDropTarget(
                            { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = object : DragAndDropTarget {
                                override fun onDrop(event: DragAndDropEvent): Boolean {

                                    val rawX = event.toAndroidDragEvent().x
                                    val rawY = event.toAndroidDragEvent().y

                                    Log.i("RawX = ", "$rawX")
                                    Log.i("RawY = ", "$rawY")
                                    val canvasPosition = Offset(
                                        x = (rawX + offset.value.x) / currentScale.value,
                                        y = (rawY + offset.value.y) / currentScale.value
                                    )
                                    Log.i("scale", currentScale.value.toString())
                                    Log.i("canvasPosition.x = ", "${canvasPosition.x}")
                                    Log.i("canvasPosition.y = ", "${canvasPosition.y}")

                                    val clipData = event.toAndroidDragEvent().clipData ?: return false
                                    if (clipData.itemCount == 0) return false

                                    val blockType = clipData.getItemAt(0).text?.toString() ?: return false

                                    val newBlock = when (blockType) {
                                        "IntLiteralBlock" -> IntLiteralBlock()
                                        "SetValueVariableBlock" -> SetValueVariableBlock()
                                        "VariableDeclarationBlock" -> VariableDeclarationBlock()
                                        "VariableReferenceBlock" -> VariableReferenceBlock()
                                        else -> {IntLiteralBlock()}
                                    }

                                    viewModel.addBlock(
                                        DraggableBlock(
                                            newBlock.id.toString(),
                                            newBlock,
                                            canvasPosition.x,
                                            canvasPosition.y,
                                            width = if (blockType == "IntLiteralBlock") 100 else 200
                                        )
                                    )
                                    scope.launch { drawerState.close() }
                                    return true
                                }
                            }
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
                                blocks
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