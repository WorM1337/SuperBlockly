package com.unewexp.superblockly.viewBlocks

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.enums.BlockType




object ViewInitialSize {
    data class PairDp(var x: Dp, var y: Dp)

    val cornerOffset: Dp = 15.dp

    val sizeDictionary: Map<BlockType, PairDp> = mapOf( // При передаче для input-string и output-string нужно указывать не
    // проценты, а константу
        BlockType.OPERAND
                to PairDp(x = 200.dp, y = 60.dp),
        BlockType.SET_VARIABLE_VALUE
                to PairDp(x = 200.dp, y = 60.dp),
        BlockType.START
                to PairDp(x = 300.dp, y = 200.dp),
        BlockType.VARIABLE_DECLARATION
                to PairDp(x = 200.dp, y = 60.dp),
        BlockType.INT_LITERAL
                to PairDp(x = 100.dp, y = 60.dp),
        BlockType.STRING_LITERAL
                to PairDp(x = 100.dp, y = 60.dp),
        BlockType.BOOLEAN_LITERAL
                to PairDp(x = 100.dp, y = 60.dp),
        BlockType.VARIABLE_REFERENCE
                to PairDp(x = 100.dp, y = 60.dp),
        BlockType.STRING_CONCAT
                to PairDp(x = 240.dp, y = 60.dp),
        BlockType.STRING_APPEND
                to PairDp(x = 240.dp, y = 60.dp),
        BlockType.PRINT_BLOCK
                to PairDp(x = 150.dp, y = 60.dp),
        BlockType.IF_BLOCK
                to PairDp(x = 100.dp, y = 60.dp)
    )
}