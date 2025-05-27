package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.ExtendConnectionViewType

object SizeManager {
    fun changeParentParams(child: DraggableBlock, isPositive: Boolean = true){ // Вызывается при непосредственном добавлении блока в какой-то другой

        val parent = child.connectedParent

        if(parent == null) return

        val plusOrMinus = if(isPositive) 1 else -1

        val connectionView = child.connectedParentConnectionView



        if(parent.isInner && connectionView!!.extendType == ExtendConnectionViewType.NONE){

            var currentBlock = parent
            while(currentBlock!!.connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_INNER){
                Log.i("INFO_FOR_ME", "${child.block.blockType}")
                currentBlock = currentBlock.connectedParent!!
            }
            currentBlock = currentBlock.connectedParent!!
            val deltaHeight = CalculationsManager.getSummaryHeight(child)
            currentBlock.inputConnectionViews.forEach{ connection ->
                if(connection.positionY > connectionView.positionY){
                    connection.positionY += deltaHeight * plusOrMinus
                }
            }
            val oldHeight = currentBlock.height.value

            currentBlock.height.value += deltaHeight * plusOrMinus

            val diffrenceHeight = if(currentBlock.height.value - oldHeight < 0.dp) oldHeight - currentBlock.height.value else currentBlock.height.value - oldHeight

            changeParentParams(currentBlock, deltaHeight = diffrenceHeight, isPositive = isPositive)
            return
        }

        val oldHeight = parent.height.value
        val oldWidth = parent.width.value


        when(connectionView!!.extendType){
            ExtendConnectionViewType.SIDE -> {
                val delta = if(child.height.value - parent.height.value > 0.dp) child.height.value - parent.height.value else 0.dp
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionY > connectionView.positionY){
                        connection.positionY += delta * plusOrMinus
                    }
                }

                parent.height.value += delta * plusOrMinus

                val diffrenceHeight = if(parent.height.value - oldHeight < 0.dp) oldHeight - parent.height.value else parent.height.value - oldHeight

                changeParentParams(parent, deltaHeight = diffrenceHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.INNER -> {
                val deltaWidth = child.width.value
                val deltaHeight = child.height.value
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionX > connectionView.positionX && connection.positionY > connectionView.positionY){
                        connection.positionX += deltaWidth * plusOrMinus
                        connection.positionY += deltaHeight * plusOrMinus
                    }
                }

                parent.height.value += deltaHeight * plusOrMinus
                parent.width.value += deltaWidth * plusOrMinus

                val diffrenceHeight = if(parent.height.value - oldHeight < 0.dp) oldHeight - parent.height.value else parent.height.value - oldHeight
                val diffrenceWidth = if(parent.width.value - oldWidth < 0.dp) oldWidth - parent.width.value else parent.width.value - oldWidth

                changeParentParams(parent, diffrenceWidth, diffrenceHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.INNER_BOTTOM -> {
                val deltaHeight = CalculationsManager.getSummaryHeight(child)
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionY > connectionView.positionY){
                        connection.positionY += deltaHeight * plusOrMinus
                    }
                }

                parent.height.value += deltaHeight * plusOrMinus

                val diffrenceHeight = if(parent.height.value - oldHeight < 0.dp) oldHeight - parent.height.value else parent.height.value - oldHeight

                changeParentParams(parent, deltaHeight = diffrenceHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.NONE -> {}
        }
    }
    fun changeParentParams(child: DraggableBlock, deltaWidth: Dp = 0.dp, deltaHeight: Dp = 0.dp, isPositive: Boolean = true){ // Вызывается рекурсивно из верхней
        // перегрузки для пересчета размеров родительских блоков

        val parent = child.connectedParent

        if(parent == null) return

        val plusOrMinus = if(isPositive) 1 else -1

        val connectionView = child.connectedParentConnectionView

        if(parent.isInner && connectionView!!.extendType == ExtendConnectionViewType.NONE){

            var currentBlock = parent
            while(currentBlock!!.connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_INNER){
                currentBlock = currentBlock.connectedParent!!
            }
            currentBlock = currentBlock.connectedParent!!
            currentBlock.inputConnectionViews.forEach{ connection ->
                if(connection.positionY > connectionView.positionY){
                    connection.positionY += deltaHeight * plusOrMinus
                }
            }
            currentBlock.height.value += deltaHeight * plusOrMinus
            changeParentParams(currentBlock, deltaWidth, deltaHeight, isPositive = isPositive)
            return
        }

        when(connectionView!!.extendType){
            ExtendConnectionViewType.SIDE -> {
                val delta = if(child.height.value - parent.height.value > 0.dp) child.height.value - parent.height.value else 0.dp
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionY > connectionView.positionY){
                        connection.positionY += delta * plusOrMinus
                    }
                }
                parent.height.value += delta * plusOrMinus
                changeParentParams(parent, deltaHeight = delta, isPositive = isPositive)
            }
            ExtendConnectionViewType.INNER -> {
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionX > connectionView.positionX && connection.positionY > connectionView.positionY){
                        connection.positionX += deltaWidth * plusOrMinus
                        connection.positionY += deltaHeight * plusOrMinus
                    }
                }
                parent.height.value += deltaHeight * plusOrMinus
                parent.width.value += deltaWidth * plusOrMinus
                changeParentParams(parent, deltaWidth, deltaHeight, isPositive = isPositive)

            }
            ExtendConnectionViewType.INNER_BOTTOM -> {
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionY > connectionView.positionY){
                        connection.positionY += deltaHeight * plusOrMinus
                    }
                }
                parent.height.value += deltaHeight * plusOrMinus
                changeParentParams(parent, deltaHeight = deltaHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.NONE -> {}
        }
    }
}