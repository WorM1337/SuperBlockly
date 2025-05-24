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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.model.ConnectorManager
import com.unewexp.superblockly.viewBlocks.DeclarationVariableView
import com.unewexp.superblockly.viewBlocks.DraggableBase
import com.unewexp.superblockly.viewBlocks.DraggableBlock
import com.unewexp.superblockly.viewBlocks.IfBlockView
import com.unewexp.superblockly.viewBlocks.IntLiteralView
import com.unewexp.superblockly.viewBlocks.SetValueVariableView
import com.unewexp.superblockly.viewBlocks.VariableReferenceView
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

    fun dpToPx(dp: Dp): Float {
        val pxValue = with(density) {dp.toPx()}  // Упрощённый расчёт

        return pxValue
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth().background(Color.White)
            )
            Row{
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
            ){
                IconButton(
                    onClick = {TODO("Запуск программы")},
                    modifier =
                        Modifier
                            .border(3.dp, Color.Green, CircleShape)){
                    Icon(Icons.Filled.PlayArrow, null)
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
                    Log.i("render", "${it.block.blockType} with id: " + it.id)
                    DraggableBase(
                        content = {
                            TakeViewBlock(it, viewModel)
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
                            viewModel.updateBlockPosition(it, offsetX, offsetY)
                        },
                        onDoubleTap = {
                            viewModel.removeBlock(it)
                        },
                        onDragEnd = {
                            ConnectorManager.tryConnectAndDisconnectDrag(it, viewModel, density)
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


@Composable
fun TakeViewBlock (block: DraggableBlock, viewModel: DraggableViewModel = viewModel()){
    val blockType = block.block.blockType
    when(blockType){
        BlockType.OPERAND -> TODO()
        BlockType.SET_VARIABLE_VALUE -> SetValueVariableView { newValue ->
            viewModel.updateValue(block, newValue)
        }

        BlockType.START -> TODO()
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