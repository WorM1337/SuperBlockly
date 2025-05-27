package com.unewexp.superblockly.model

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.model.ConnectionView
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.enums.ConnectorType
import kotlin.math.pow
import kotlin.math.sqrt

object CalculationsManager {
    fun getLengthFromConnections(
        dragBlock1: DraggableBlock,
        dragBlock2: DraggableBlock,
        connection1: ConnectionView,
        connection2: ConnectionView,
        density: Density
    ): Double {

        val con1x = with(density) {connection1.positionX.toPx()}
        val con1y = with(density) {connection1.positionY.toPx()}
        val con2x = with(density) {connection2.positionX.toPx()}
        val con2y = with(density) {connection2.positionY.toPx()}

        val x1 = (dragBlock1.x.value + con1x)
        val y1 = (dragBlock1.y.value + con1y)
        val x2 = (dragBlock2.x.value + con2x)
        val y2 = (dragBlock2.y.value + con2y)

        return sqrt((x1 - x2).toDouble().pow(2) + (y1 - y2).toDouble().pow(2))
    }

    fun getSummaryHeight(block: DraggableBlock): Dp {
        var ans: Dp = 0.dp

        var current = block

        val currentList: MutableList<DraggableBlock> = mutableListOf()

        while(true){

            currentList.add(current)

            var flag = false

            for(child in current.scope){
                if(child.connectedParentConnectionView!!.connector.connectionType == ConnectorType.STRING_BOTTOM_OUTER){
                    flag = true
                    current = child
                }
            }
            if(!flag) break
        }
        for(item in currentList){
            ans += item.height.value
        }
        return ans
    }
}