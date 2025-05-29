package com.unewexp.superblockly.viewBlocks

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.enums.BlockType




object ViewInitialSize {
    data class PairDp(var width: Dp, var height: Dp)

    val cornerOffset: Dp = 15.dp
    val defaultHeight = 60.dp
    val defaultWidth = 20.dp

    fun getInitialSizeByBlockType(blockType: BlockType) : PairDp?{
        return when(blockType){
            BlockType.SET_VARIABLE_VALUE ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.START ->
                PairDp(width = 200.dp, height = 100.dp)
            BlockType.INT_LITERAL ->
                PairDp(width = 100.dp, height = 60.dp)
            BlockType.STRING_LITERAL ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.BOOLEAN_LITERAL ->
                PairDp(width = 100.dp, height = 60.dp)
            BlockType.OPERAND ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.SHORTHAND_ARITHMETIC_BLOCK ->
                PairDp(width = 150.dp, height = 60.dp)
            BlockType.VARIABLE_DECLARATION ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.VARIABLE_REFERENCE ->
                PairDp(width = 150.dp, height = 60.dp)
            BlockType.STRING_CONCAT ->
                PairDp(width = 240.dp, height = 60.dp)
            BlockType.STRING_APPEND ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.PRINT_BLOCK ->
                PairDp(width = 100.dp, height = 60.dp)
            BlockType.COMPARE_NUMBERS_BLOCK ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.BOOLEAN_LOGIC_BLOCK ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.NOT_BLOCK ->
                PairDp(width = 100.dp, height = 60.dp)
            BlockType.IF_BLOCK ->
                PairDp(width = 100.dp, height = 140.dp)
            BlockType.ELSE_BLOCK ->
                PairDp(width = 100.dp, height = 140.dp)
            BlockType.IF_ELSE_BLOCK ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.REPEAT_N_TIMES ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.WHILE_BLOCK ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.FOR_BLOCK ->
                PairDp(width = 400.dp, height = 140.dp)
            BlockType.FOR_ELEMENT_IN_LIST ->
                PairDp(width = 200.dp, height = 140.dp)
            BlockType.FIXED_VALUE_AND_SIZE_LIST ->
                PairDp(width = 400.dp, height = 60.dp)
            BlockType.GET_VALUE_BY_INDEX ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.REMOVE_VALUE_BY_INDEX ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.ADD_VALUE_BY_INDEX ->
                PairDp(width = 200.dp, height = 60.dp)
            BlockType.GET_LIST_SIZE ->
                PairDp(width = 100.dp, height = 60.dp)
            else -> null
        }
    }
}