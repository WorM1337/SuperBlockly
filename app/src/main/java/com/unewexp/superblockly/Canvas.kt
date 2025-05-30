package com.unewexp.superblockly

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.debug.ConsolePanel
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.viewBlocks.VariableDeclarationBlockView
import com.unewexp.superblockly.viewBlocks.DraggableBase
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.logic.BooleanLogicBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.debug.DebugController
import com.unewexp.superblockly.debug.DebugPanel
import com.unewexp.superblockly.debug.ExecutionContext
import com.unewexp.superblockly.debug.RunProgram
import com.unewexp.superblockly.ui.theme.ActiveRunProgram
import com.unewexp.superblockly.ui.theme.ConnectorColor
import com.unewexp.superblockly.ui.theme.canvasBackground
import com.unewexp.superblockly.ui.theme.stopProgram
import com.unewexp.superblockly.ui.theme.topBarBackground
import com.unewexp.superblockly.viewBlocks.AddElementByIndexView
import com.unewexp.superblockly.viewBlocks.BooleanLiteralBlockView
import com.unewexp.superblockly.viewBlocks.BooleanLogicBlockView
import com.unewexp.superblockly.viewBlocks.CompareNumbersBlockView
import com.unewexp.superblockly.viewBlocks.EditValueByIndexView
import com.unewexp.superblockly.viewBlocks.ElseBlockView
import com.unewexp.superblockly.viewBlocks.ElseIfBlockView
import com.unewexp.superblockly.viewBlocks.FixedValuesAndSizeListView
import com.unewexp.superblockly.viewBlocks.ForBlockView
import com.unewexp.superblockly.viewBlocks.ForElementInListBlockView
import com.unewexp.superblockly.viewBlocks.GetListSizeView
import com.unewexp.superblockly.viewBlocks.GetValueByIndexView
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.NotBlockView
import com.unewexp.superblockly.viewBlocks.OperandBlockView
import com.unewexp.superblockly.viewBlocks.PrintBlockView
import com.unewexp.superblockly.viewBlocks.PushBackElementView
import com.unewexp.superblockly.viewBlocks.RemoveValueByIndexView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.StartBlockView
import com.unewexp.superblockly.viewBlocks.StringConcatenationBlockView
import com.unewexp.superblockly.viewBlocks.StringLiteralBlockView
import com.unewexp.superblockly.viewBlocks.TopConnector
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
import com.unewexp.superblockly.viewBlocks.WhileBlockView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun Canvas(
    openDrawer: () -> Unit,
    onHomeClick: @Composable () -> Unit,
    updateOffset: (newOffset: Offset) -> Unit,
    viewModel: DraggableViewModel = viewModel(),
) {
    val density = LocalDensity.current
    val zoomFactor = 0.7f
    val globalOffset = remember { mutableStateOf(Offset.Zero) }
    val blocks by viewModel.blocks.collectAsState()

    val iconButtonSize = 34.dp
    val iconImageSize = 30.dp
    val cornersButton = 5.dp

    var panelIsVisible by remember { mutableStateOf(true) }

    val isActive =
        ExecutionContext.programProgress == RunProgram.RUN || ExecutionContext.programProgress == RunProgram.DEBUG
    val backgroundColorIfActive = if (isActive) ActiveRunProgram else Color.Transparent

    fun startExecution() {
        panelIsVisible = true
        ExecutionContext.executionJob = CoroutineScope(Dispatchers.Main).launch {
            blocks[0].block.execute()
        }
    }

    fun stopExecution() {
        ExecutionContext.executionJob?.cancel()
        ExecutionContext.programProgress = RunProgram.NONE
        DebugController.reset()
    }


    val core = DraggableBlock(
        StartBlock(),
        mutableStateOf(100f),
        mutableStateOf(100f)
    )

    if (blocks.isEmpty()) {
        viewModel.handleAction(
            DraggableViewModel.BlocklyAction.AddBlock(core)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(topBarBackground)
                .zIndex(100000f)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp)
            ) {
                IconButton(onClick = openDrawer) {
                    Icon(Icons.Filled.List, contentDescription = "Меню")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row() {

                        if (ExecutionContext.programProgress != RunProgram.DEBUG) {
                            Box(contentAlignment = Alignment.Center) {
                                IconButton(
                                    onClick = {
                                        ExecutionContext.programProgress = RunProgram.RUN
                                        startExecution()
                                    },
                                    enabled = (
                                            if (ExecutionContext.programProgress == RunProgram.RUN) {
                                                false
                                            } else {
                                                true
                                            }
                                            ),
                                    modifier = Modifier
                                        .size(iconButtonSize)
                                        .background(
                                            backgroundColorIfActive,
                                            RoundedCornerShape(cornersButton)
                                        ),
                                    content = {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.start_program_button_green),
                                                contentDescription = "Запуск программы",
                                                modifier = Modifier.size(iconImageSize)
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(22.dp))

                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    ExecutionContext.programProgress = RunProgram.DEBUG
                                    startExecution()
                                },
                                modifier = Modifier
                                    .size(iconButtonSize)
                                    .background(backgroundColorIfActive, RoundedCornerShape(5.dp)),
                                enabled = (
                                        if (ExecutionContext.programProgress == RunProgram.NONE) {
                                            true
                                        } else {
                                            false
                                        }
                                        ),
                                content = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        if (ExecutionContext.programProgress == RunProgram.DEBUG) {
                                            Image(
                                                painter = painterResource(id = R.drawable.debug_icon_white),
                                                contentDescription = "Отладка",
                                                modifier = Modifier.size(iconImageSize)
                                            )
                                        } else {
                                            Image(
                                                painter = painterResource(id = R.drawable.debug_icon_green),
                                                contentDescription = "Отладка",
                                                modifier = Modifier.size(iconImageSize)
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        if (ExecutionContext.programProgress != RunProgram.NONE) {
                            Spacer(modifier = Modifier.size(22.dp))
                            Box(

                            ) {
                                IconButton(
                                    onClick = {
                                        stopExecution()
                                    },
                                    modifier = Modifier
                                        .size(iconButtonSize)
                                        .background(stopProgram, RoundedCornerShape(5.dp))
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.stop_icon_white),
                                        contentDescription = "Остановить программу",
                                        modifier = Modifier.size(iconImageSize)
                                    )
                                }
                            }
                        }


                    }
                    Spacer(modifier = Modifier.size(22.dp))
                    Row {
                        Box(
                            Modifier

                        ) {
                            IconButton(
                                onClick = { panelIsVisible = !panelIsVisible },
                                modifier = Modifier.size(iconButtonSize)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.console_icon_blue),
                                    contentDescription = "Открыть/Закрыть консоль",
                                    modifier = Modifier.size(iconImageSize)
                                )
                            }
                        }

                        if (ExecutionContext.programProgress == RunProgram.DEBUG) {
                            Spacer(modifier = Modifier.size(22.dp))
                            Box() {
                                IconButton(
                                    onClick = {
                                        DebugController.continueExecution()
                                    },
                                    modifier = Modifier.size(42.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.step_to_button_blue),
                                        contentDescription = "Следующий блок",
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp)
            ) {
                onHomeClick()
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            val scale = remember { mutableFloatStateOf(1f) }
            val currentScale = remember { mutableStateOf(1f) }

            Box(
                modifier = Modifier
                    .width(4000.dp)
                    .height(2000.dp)
                    .background(canvasBackground)

                    .transformable(
                        state = rememberTransformableState { zoomChange, offsetChange, _ ->
                            scale.value *= 1f + (zoomChange - 1f) * zoomFactor
                            currentScale.value += scale.floatValue - 1
                            globalOffset.value += offsetChange
                            updateOffset(globalOffset.value)
                        }
                    )
                    .graphicsLayer(
                        scaleX = scale.value,
                        scaleY = scale.value,
                        translationX = globalOffset.value.x,
                        translationY = globalOffset.value.y
                    )
            ) {
                blocks.forEach { it ->
                    DraggableBase(
                        content = {

                            TakeViewBlock(it, viewModel)

                            if (it.block.blockType != BlockType.START) {
                                TopConnector(
                                    modifier = Modifier
                                        .offset(
                                            it.outputConnectionView!!.positionX - 10.dp - 16.dp,
                                            it.outputConnectionView!!.positionY - 12.dp - 8.dp
                                        ),
                                    color = if (it.connectedParent != null) ConnectorColor else Color.Gray.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }

                        },
                        it,
                        onPositionChanged = { offsetX, offsetY ->
                            viewModel.handleAction(
                                DraggableViewModel.BlocklyAction.MoveBlock(
                                    it, offsetX, offsetY
                                )
                            )
                        },
                        onDoubleTap = {
                            viewModel.handleAction(DraggableViewModel.BlocklyAction.RemoveBlock(it))
                        },
                        onDragStart = {
                            it.zIndex.value = viewModel.maxZIndex + 0.1f
                            val queue: MutableList<DraggableBlock> = mutableListOf()
                            it.scope.forEach { block ->
                                queue.add(block)
                            }
                            while (!queue.isEmpty()) {
                                val item = queue.first()
                                queue.removeAt(0)
                                item.scope.forEach { element ->
                                    queue.add(element)
                                }
                                item.zIndex.value = item.connectedParent!!.zIndex.value + 0.1f
                                viewModel.maxZIndex = max(item.zIndex.value, viewModel.maxZIndex)
                            }
                        },
                        onDragEnd = {
                            if (it.block !is StartBlock) {
                                ConnectorManager.tryConnectAndDisconnectDrag(it, viewModel, density)
                                it.connectedParent?.let { parent ->
                                    val queue: MutableList<DraggableBlock> = mutableListOf(parent)
                                    while (!queue.isEmpty()) {
                                        val item = queue.first()
                                        queue.removeAt(0)
                                        item.scope.forEach { element ->
                                            queue.add(element)
                                            element.zIndex.value = item.zIndex.value + 0.1f
                                            viewModel.maxZIndex =
                                                max(element.zIndex.value, viewModel.maxZIndex)
                                        }
                                    }
                                }
                            }
                            viewModel.normalizeZIndex()
                        }
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (ExecutionContext.programProgress != RunProgram.DEBUG && panelIsVisible) {
                    ConsolePanel()
                }
                if (ExecutionContext.programProgress == RunProgram.DEBUG && panelIsVisible) {
                    DebugPanel()
                }
            }
        }
    }
}


@Composable
fun TakeViewBlock(block: DraggableBlock, viewModel: DraggableViewModel = viewModel()) {
    val blockType = block.block.blockType
    when (blockType) {
        BlockType.OPERAND -> OperandBlockView(
            { type ->
                (block.block as OperandBlock).operand = type

            },
            block
        )

        BlockType.SET_VARIABLE_VALUE -> SetValueVariableView(block)

        BlockType.START -> StartBlockView()
        BlockType.VARIABLE_DECLARATION -> VariableDeclarationBlockView(block)

        BlockType.INT_LITERAL -> IntLiteralView(block)

        BlockType.STRING_LITERAL -> StringLiteralBlockView(block)
        BlockType.BOOLEAN_LITERAL -> BooleanLiteralBlockView(block)
        BlockType.VARIABLE_REFERENCE -> VariableReferenceView(block)

        BlockType.STRING_CONCAT -> StringConcatenationBlockView(block)
        BlockType.STRING_APPEND -> TODO()
        BlockType.PRINT_BLOCK -> PrintBlockView()
        BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
        BlockType.COMPARE_NUMBERS_BLOCK -> CompareNumbersBlockView(
            block,
            { type ->
                (block.block as CompareNumbers).compareType = type
            }
        )

        BlockType.BOOLEAN_LOGIC_BLOCK -> BooleanLogicBlockView(block) { type ->
            (block.block as BooleanLogicBlock).logicOperand = type
        }

        BlockType.NOT_BLOCK -> NotBlockView()
        BlockType.IF_BLOCK -> IfBlockView()
        BlockType.ELSE_BLOCK -> ElseBlockView()
        BlockType.IF_ELSE_BLOCK -> ElseIfBlockView()
        BlockType.REPEAT_N_TIMES -> TODO()
        BlockType.WHILE_BLOCK -> WhileBlockView()
        BlockType.FOR_BLOCK -> ForBlockView(block)
        BlockType.FOR_ELEMENT_IN_LIST -> ForElementInListBlockView(block)
        BlockType.FIXED_VALUE_AND_SIZE_LIST -> FixedValuesAndSizeListView(block)
        BlockType.GET_VALUE_BY_INDEX -> GetValueByIndexView(block)
        BlockType.REMOVE_VALUE_BY_INDEX -> RemoveValueByIndexView(block)
        BlockType.ADD_VALUE_BY_INDEX -> AddElementByIndexView(block)
        BlockType.GET_LIST_SIZE -> GetListSizeView()
        BlockType.EDIT_VALUE_BY_INDEX -> EditValueByIndexView(block)
        BlockType.PUSH_BACK_ELEMENT -> PushBackElementView(block)
    }
}