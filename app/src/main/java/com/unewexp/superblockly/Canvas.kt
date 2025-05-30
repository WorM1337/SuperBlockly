package com.unewexp.superblockly

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.debug.ConsolePanel
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.viewBlocks.BottomConnector
import com.unewexp.superblockly.viewBlocks.DeclarationVariableView
import com.unewexp.superblockly.viewBlocks.DraggableBase
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.enums.symbol
import com.unewexp.superblockly.viewBlocks.AddElementByIndexView
import com.unewexp.superblockly.viewBlocks.CompareNumbersBlockView
import com.unewexp.superblockly.viewBlocks.ElseBlockView
import com.unewexp.superblockly.viewBlocks.ElseIfBlockView
import com.unewexp.superblockly.viewBlocks.FixedValuesAndSizeListView
import com.unewexp.superblockly.viewBlocks.ForBlockView
import com.unewexp.superblockly.viewBlocks.GetListSizeView
import com.unewexp.superblockly.viewBlocks.GetValueByIndexView
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.OperandBlockView
import com.unewexp.superblockly.viewBlocks.PrintBlockView
import com.unewexp.superblockly.viewBlocks.RemoveValueByIndexView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.StartBlockView
import com.unewexp.superblockly.viewBlocks.TopConnector
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
import com.unewexp.superblockly.viewBlocks.WhileBlockView
import java.util.Queue
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun Canvas(
    openDrawer: () -> Unit,
    onHomeClick: @Composable () -> Unit,
    updateOffset: (newOffset: Offset) -> Unit,
    viewModel: DraggableViewModel = viewModel()
){

    val density = LocalDensity.current
    val zoomFactor = 0.7f
    val globalOffset = remember { mutableStateOf(Offset.Zero) }
    val blocks by viewModel.blocks.collectAsState()

    var panelIsVisible by remember { mutableStateOf(true) }

    val core = DraggableBlock(
        StartBlock(),
        mutableStateOf(100f),
        mutableStateOf(100f))

    if(blocks.isEmpty()){
        viewModel.handleAction(
            DraggableViewModel.BlocklyAction.AddBlock(core)
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.White).zIndex(100000f)
        ) {
            Row {
                Box {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Filled.List, null)
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row{
                    Box(
                        Modifier
                            .padding(2.dp)
                    ){
                        IconButton(
                            onClick = { panelIsVisible = !panelIsVisible },
                            modifier =
                                Modifier
                                    .border(3.dp, Color.Black, CircleShape)
                        ) {
                            Icon(Icons.Filled.Info, null)
                        }
                    }
                    Box(
                        Modifier
                            .padding(2.dp)
                    ){
                        IconButton(
                            onClick = { blocks[0].block.execute() },
                            modifier =
                                Modifier
                                    .border(3.dp, Color.Green, CircleShape)
                        ) {
                            Icon(Icons.Filled.PlayArrow, null)
                        }
                    }

                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 5.dp, 0.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row{
                    onHomeClick()
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth()){
            val scale = remember { mutableFloatStateOf(1f) }
            val currentScale = remember{ mutableStateOf(1f) }

            Box(
                modifier = Modifier
                    .width(4000.dp)
                    .height(2000.dp)
                    .background(Color.LightGray)

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
                                    color = if (it.connectedParent != null) getColorByBlockType(it.block.blockType) else Color.Gray.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }

                        },
                        it,
                        onPositionChanged = { offsetX, offsetY ->
                            viewModel.handleAction(DraggableViewModel.BlocklyAction.MoveBlock(
                                it, offsetX, offsetY
                            ))
                        },
                        onDoubleTap = {
                            viewModel.handleAction(DraggableViewModel.BlocklyAction.RemoveBlock(it))
                        },
                        onDragStart = {
                            val queue: MutableList<DraggableBlock> = mutableListOf(it)
                            while(!queue.isEmpty()){
                                val item = queue.first()
                                queue.removeAt(0)
                                item.scope.forEach { element ->
                                    queue.add(element)
                                }
                                item.zIndex.value += viewModel.maxZIndex + 0.1f
                                viewModel.maxZIndex = max(item.zIndex.value, viewModel.maxZIndex)
                            }
                        },
                        onDragEnd = {
                            if (it.block !is StartBlock){
                                ConnectorManager.tryConnectAndDisconnectDrag(it, viewModel, density)
                                it.connectedParent?.let{ parent ->
                                    val queue: MutableList<DraggableBlock> = mutableListOf(parent)
                                    while(!queue.isEmpty()){
                                        val item = queue.first()
                                        queue.removeAt(0)
                                        item.scope.forEach { element ->
                                            queue.add(element)
                                            element.zIndex.value = item.zIndex.value + 0.1f
                                            viewModel.maxZIndex = max(element.zIndex.value, viewModel.maxZIndex)
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
            ){
                if(panelIsVisible){
                    ConsolePanel()
                }
            }
        }
    }
}


@Composable
fun TakeViewBlock (block: DraggableBlock, viewModel: DraggableViewModel = viewModel()){
    val blockType = block.block.blockType
    when(blockType){
        BlockType.OPERAND -> OperandBlockView (
            { type ->
            (block.block as OperandBlock).operand = type

        },
            block
        )
        BlockType.SET_VARIABLE_VALUE -> SetValueVariableView { newValue ->
            viewModel.updateValue(block, newValue)
        }

        BlockType.START -> StartBlockView()
        BlockType.VARIABLE_DECLARATION -> DeclarationVariableView { newValue ->
            viewModel.updateValue(block, newValue)
        }

        BlockType.INT_LITERAL -> IntLiteralView { newValue ->
            viewModel.updateValue(block, newValue)
        }

        BlockType.STRING_LITERAL -> TODO()
        BlockType.BOOLEAN_LITERAL -> TODO()
        BlockType.VARIABLE_REFERENCE -> VariableReferenceView { newValue ->
            viewModel.updateValue(block, newValue)
        }

        BlockType.STRING_CONCAT -> TODO()
        BlockType.STRING_APPEND -> TODO()
        BlockType.PRINT_BLOCK -> PrintBlockView()
        BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
        BlockType.COMPARE_NUMBERS_BLOCK -> CompareNumbersBlockView(
            { type ->
                (block.block as CompareNumbers).compareType = type
            }
        )
        BlockType.BOOLEAN_LOGIC_BLOCK -> TODO()
        BlockType.NOT_BLOCK -> TODO()
        BlockType.IF_BLOCK -> IfBlockView()
        BlockType.ELSE_BLOCK -> ElseBlockView()
        BlockType.IF_ELSE_BLOCK -> ElseIfBlockView()
        BlockType.REPEAT_N_TIMES -> TODO()
        BlockType.WHILE_BLOCK -> WhileBlockView()
        BlockType.FOR_BLOCK -> ForBlockView { newValue ->
            viewModel.updateValue(block, newValue)
        }
        BlockType.FOR_ELEMENT_IN_LIST -> TODO()
        BlockType.FIXED_VALUE_AND_SIZE_LIST -> FixedValuesAndSizeListView(block)
        BlockType.GET_VALUE_BY_INDEX -> GetValueByIndexView(block)
        BlockType.REMOVE_VALUE_BY_INDEX -> RemoveValueByIndexView(block)
        BlockType.ADD_VALUE_BY_INDEX -> AddElementByIndexView(block)
        BlockType.GET_LIST_SIZE -> GetListSizeView()
        BlockType.EDIT_VALUE_BY_INDEX -> TODO()
        BlockType.PUSH_BACK_ELEMENT -> TODO()
    }
}