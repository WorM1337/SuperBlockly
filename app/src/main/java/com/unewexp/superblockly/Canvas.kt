package com.unewexp.superblockly

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.debug.ConsolePanel
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.viewBlocks.BottomConnector
import com.unewexp.superblockly.viewBlocks.DeclarationVariableView
import com.unewexp.superblockly.viewBlocks.DraggableBase
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.PrintBlockView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.StartBlockView
import com.unewexp.superblockly.viewBlocks.TopConnector
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
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

    val core = DraggableBlock(
        StartBlock(),
        mutableStateOf(100f),
        mutableStateOf(100f))

    if(blocks.isEmpty()){
        viewModel.addBlock(core)
    }

    fun dpToPx(dp: Dp): Float {
        val pxValue = with(density) {dp.toPx()}  // Упрощённый расчёт

        return pxValue
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.White)
            ) {
                Row {
                    Box {
                        IconButton(onClick = openDrawer) {
                            Icon(Icons.Filled.List, null)
                        }
                    }
                    Box {
                        onHomeClick()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 5.dp, 0.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = { core.block.execute() },
                        modifier =
                            Modifier
                                .border(3.dp, Color.Green, CircleShape)
                    ) {
                        Icon(Icons.Filled.PlayArrow, null)
                    }
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
                    Log.i("render", "${it.block.blockType} with id: " + it.block.id)
                    DraggableBase(
                        content = {

                            TakeViewBlock(it, viewModel)

                            if (it.block.blockType != BlockType.START) {
                                TopConnector(
                                    modifier = Modifier
                                        .offset(
                                            it.outputConnectionView!!.positionX - 10.dp - 8.dp,
                                            it.outputConnectionView!!.positionY - 12.dp - 4.dp
                                        ),
                                    color = if (it.connectedParent != null) getColorByBlockType(it.block.blockType) else Color.Gray.copy(
                                        alpha = 0.5f
                                    )
                                )
                            }

                        },
                        it,
                        onPositionChanged = { offsetX, offsetY ->
                            viewModel.updateBlockPosition(it, offsetX, offsetY)
                        },
                        onDoubleTap = {
                            viewModel.removeBlock(it)
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
                ConsolePanel(
                    height = LocalConfiguration.current.screenHeightDp.dp - 50.dp
                )
            }
        }
    )
}


@Composable
fun TakeViewBlock (block: DraggableBlock, viewModel: DraggableViewModel = viewModel()){
    val blockType = block.block.blockType
    when(blockType){
        BlockType.OPERAND -> TODO()
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
        BlockType.COMPARE_NUMBERS_BLOCK -> TODO()
        BlockType.BOOLEAN_LOGIC_BLOCK -> TODO()
        BlockType.NOT_BLOCK -> TODO()
        BlockType.IF_BLOCK -> IfBlockView()
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