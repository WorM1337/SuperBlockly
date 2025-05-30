@file:OptIn(ExperimentalFoundationApi::class)

package com.unewexp.superblockly

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.ui.theme.DrawerColor
import com.unewexp.superblockly.ui.theme.SuperBlocklyTheme
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.list.AddElementByIndex
import com.unewexp.superblockly.blocks.list.EditValueByIndex
import com.unewexp.superblockly.blocks.list.FixedValuesAndSizeList
import com.unewexp.superblockly.blocks.list.GetListSize
import com.unewexp.superblockly.blocks.list.GetValueByIndex
import com.unewexp.superblockly.blocks.list.PushBackElement
import com.unewexp.superblockly.blocks.list.RemoveValueByIndex
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.BooleanLogicBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.blocks.logic.ElseBlock
import com.unewexp.superblockly.blocks.logic.ElseIfBlock
import com.unewexp.superblockly.blocks.logic.NotBlock
import com.unewexp.superblockly.blocks.loops.ForBlock
import com.unewexp.superblockly.blocks.loops.ForElementInListBlock
import com.unewexp.superblockly.blocks.loops.WhileBlock
import com.unewexp.superblockly.blocks.returnBlocks.StringConcatenationBlock
import com.unewexp.superblockly.debug.Logger
import com.unewexp.superblockly.ui.theme.canvasBackground
import com.unewexp.superblockly.viewBlocks.AddElementByIndexView
import com.unewexp.superblockly.viewBlocks.GetListSizeView
import com.unewexp.superblockly.viewBlocks.GetValueByIndexView
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SuperBlocklyTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = canvasBackground
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.Home.route) {

                        composable(Routes.Home.route) { Home(navController) }
                        composable(Routes.CreateProject.route) {
                            CreateNewProject(
                                navController,
                                this@MainActivity.applicationContext
                            )
                        }
                        composable(Routes.MyProjects.route) { MyProjects(navController) }
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
    ) {
        Column {
            Button(
                { navController.navigate(Routes.CreateProject.route) },
                Modifiers.homeBtnModifier
            ) {
                Text(stringResource(R.string.create_new_project))
            }
            Button(
                { navController.navigate(Routes.MyProjects.route) },
                Modifiers.homeBtnModifier
            ) {
                Text(stringResource(R.string.my_projects))
            }
            Button(
                { navController.navigate(Routes.About.route) },
                Modifiers.homeBtnModifier
            ) {
                Text(stringResource(R.string.about))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateNewProject(
    navController: NavHostController,
    context: Context,
    viewModel: DraggableViewModel = viewModel(),
) {

    val density = LocalDensity.current

    viewModel.density = density

    fun dpToPx(dp: Dp): Float {
        val pxValue = with(density) { dp.toPx() }

        return pxValue
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val gesturesEnabled = remember { mutableStateOf(false) }
    val globalOffset = remember { mutableStateOf(Offset.Zero) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled.value,
        modifier = Modifier
            .fillMaxSize(),
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(DrawerColor)
            ) {
                Button(onClick = {
                    scope.launch { drawerState.close() }
                    gesturesEnabled.value = false
                }) {
                    Text(stringResource(R.string.close))
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(5.dp, 5.dp)
                ) {
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    PrintBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(
                                        newBlock
                                    )
                                )
                            }
                        ) {
                            PrintBlockCard()
                        }
                    }
                    item {
                        Text(stringResource(R.string.math), color = Color.White)
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    IntLiteralBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(200.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            IntLiteralBlockCard()
                        }
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    OperandBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(200.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            OperandBlockCard()
                        }
                    }
                    item { Text(stringResource(R.string.variables), color = Color.White) }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    SetValueVariableBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(200.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            SetValueVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    VariableDeclarationBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(200.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            DeclarationVariableCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    VariableReferenceBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(150.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            ReferenceVariableCard()
                        }
                    }
                    item {
                        Text(stringResource(R.string.logic), color = Color.White)
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    BooleanLiteralBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            BooleanLiteralBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    BooleanLogicBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            BooleanLogicBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    CompareNumbers(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            CompareNumbersBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    NotBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            NotBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    IfBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            IfBlockCard()
                        }
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    ElseBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            ElseBlockCard()
                        }
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    ElseIfBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            ElseIfBlockCard()
                        }
                    }
                    item { Text(stringResource(R.string.strings), color = Color.White) }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    StringLiteralBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(300.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            StringLiteralBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    StringConcatenationBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(300.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            StringConcatenationBlockCard()
                        }
                    }
                    item { Text(stringResource(R.string.loops), color = Color.White) }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    WhileBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(300.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            WhileBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    ForBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            ForBlockCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    ForElementInListBlock(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(100.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            ForElementInLustBlockCard()
                        }
                    }
                    item { Text(stringResource(R.string.lists), color = Color.White) }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    FixedValuesAndSizeList(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            FixedValuesAndSizeListViewCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    AddElementByIndex(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            AddElementByIndexViewCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    GetValueByIndex(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            GetValueByIndexViewCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    GetListSize(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            GetListSizeViewCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    RemoveValueByIndex(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            RemoveValueByIndexCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    EditValueByIndex(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            EditValueByIndexCard()
                        }
                    }
                    item {
                        ListItem(
                            { offset ->
                                val newBlock = DraggableBlock(
                                    PushBackElement(),
                                    mutableStateOf(offset.x + dpToPx(200.dp) - globalOffset.value.x),
                                    mutableStateOf(offset.y - dpToPx(60.dp) - globalOffset.value.y),
                                    width = mutableStateOf(400.dp)
                                )
                                viewModel.handleAction(
                                    DraggableViewModel.BlocklyAction.AddBlock(newBlock)
                                )
                            }
                        ) {
                            PushBackElementCard()
                        }
                    }
                }
            }
        }
    ) {
        var backPressedTime: Long = 0
        BackHandler(enabled = true) {
            val currentTime = System.currentTimeMillis()
            val doubleBackPressInterval = 2000
            if (currentTime - backPressedTime < doubleBackPressInterval) {
                navController.popBackStack()
            } else {
                backPressedTime = currentTime
                Toast.makeText(context, R.string.toast_message, Toast.LENGTH_SHORT).show()
            }
        }
        Canvas(
            {
                scope.launch { drawerState.open() }
                gesturesEnabled.value = true
            },
            { toHomeBtn(navController, { Logger.clearLogs() }) },
            { newOffset ->
                globalOffset.value = newOffset
                gesturesEnabled.value = false
            }
        )
    }
}

@Composable
fun ListItem(
    onDoubleClick: (offset: Offset) -> Unit,
    content: @Composable () -> Unit,
) {

    var dragStartPosition by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                dragStartPosition = coordinates.positionInRoot()
            }
            .pointerInput(Unit) {
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
fun MyProjects(navController: NavHostController) {
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        toHomeBtn(navController)
    }
    Box(
        contentAlignment = Alignment.Center
    ) {

    }
}

@Composable
fun About(navController: NavHostController) {
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
    ) {
        Column {
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