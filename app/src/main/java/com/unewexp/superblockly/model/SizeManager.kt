package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.ExtendConnectionViewType

object SizeManager {

    private fun getTheMostHeightWithoutChild(parent: DraggableBlock, child: DraggableBlock): Dp {
        if (child !in parent.scope) throw IllegalArgumentException("Ребёнок не является в scope родителя!")

        var mxHeight = 0.dp

        for (block in parent.scope) {
            if (child.connectedParentConnectionView!!.positionY == block.connectedParentConnectionView!!.positionY && block != child) {
                val mx =
                    if (block.height.value > block.connectedParentConnectionView!!.height) block.height.value else block.connectedParentConnectionView!!.height

                mxHeight = if (mxHeight > mx) mxHeight else mx
            }
            Log.i("PositionYCon", "${block.connectedParentConnectionView!!.positionY}")
        }
        return if(mxHeight > child.connectedParentConnectionView!!.height) mxHeight else child.connectedParentConnectionView!!.height
    }
    fun changeParentParams(child: DraggableBlock, viewModel: DraggableViewModel, isPositive: Boolean = true){ // Вызывается при непосредственном добавлении блока в какой-то другой

        val parent = child.connectedParent

        if(parent == null) return

        val plusOrMinus = if(isPositive) 1 else -1

        val connectionView = child.connectedParentConnectionView!!.copy()



        if(parent.isInner && connectionView.extendType == ExtendConnectionViewType.NONE){

            var currentBlock = parent
            while(currentBlock!!.connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_INNER){
                currentBlock = currentBlock.connectedParent!!
            }
            val deltaHeight = CalculationsManager.getSummaryHeight(child)
            currentBlock = currentBlock.connectedParent!!
            currentBlock.inputConnectionViews.forEach{ connection ->
                if(connection.positionY > connectionView.positionY){
                    connection.positionY += deltaHeight * plusOrMinus
                }
            }

            currentBlock.height.value += deltaHeight * plusOrMinus

            changeParentParams(currentBlock,viewModel, deltaHeight = deltaHeight, isPositive = isPositive)
            if(!isPositive)ConnectorManager.normalizeConnectorsPositions(currentBlock,viewModel, besides = child)
            else ConnectorManager.normalizeConnectorsPositions(currentBlock,viewModel)
            return
        }

        val oldHeight = parent.height.value
        val oldWidth = parent.width.value


        when(connectionView!!.extendType){
            ExtendConnectionViewType.SIDE -> {

                val mxHeight = getTheMostHeightWithoutChild(parent, child)
                if(mxHeight <= child.height.value){
                    val delta = child.height.value - mxHeight

                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionY > connectionView.positionY){
                            connection.positionY += delta * plusOrMinus
                        }
                        else if(connection.positionY == connectionView.positionY){
                            connection.positionY += delta/2 * plusOrMinus
                        }
                    }
                    if(parent.outputConnectionView!!.positionY == connectionView.positionY){
                        parent.outputConnectionView!!.positionY += delta/2 * plusOrMinus
                    }
                    parent.height.value += delta * plusOrMinus

                    changeParentParams(parent,viewModel, deltaHeight = delta, isPositive = isPositive)
                }
            }
            ExtendConnectionViewType.INNER -> {

                val mxHeight = getTheMostHeightWithoutChild(parent, child)


                var diffrenceHeight = 0.dp
                var diffrenceWidth = 0.dp


                if(mxHeight <= child.height.value){
                    Log.i("mxHeight", "${mxHeight}")
                    val deltaHeight = child.height.value - mxHeight

                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionY > connectionView.positionY){
                            connection.positionY += deltaHeight * plusOrMinus
                        }
                        else if(connection.positionY == connectionView.positionY){
                            connection.positionY += deltaHeight/2 * plusOrMinus
                        }
                    }
                    if(parent.outputConnectionView!!.positionY == connectionView.positionY){
                        parent.outputConnectionView!!.positionY += deltaHeight/2 * plusOrMinus
                    }
                    parent.height.value += deltaHeight * plusOrMinus
                    diffrenceHeight = deltaHeight
                }
                if(child.connectedParentConnectionView!!.width <= child.width.value){
                    val deltaWidth = child.width.value - child.connectedParentConnectionView!!.width

                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionX > connectionView.positionX){
                            connection.positionX += deltaWidth * plusOrMinus
                        }
                    }
                    parent.width.value += deltaWidth * plusOrMinus

                    diffrenceWidth = deltaWidth
                }

                changeParentParams(parent,viewModel, diffrenceWidth, diffrenceHeight, isPositive = isPositive)
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

                changeParentParams(parent,viewModel, deltaHeight = diffrenceHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.NONE -> {}
        }
        if(!isPositive)ConnectorManager.normalizeConnectorsPositions(parent,viewModel, besides = child)
        else ConnectorManager.normalizeConnectorsPositions(parent,viewModel)
    }
    fun changeParentParams(child: DraggableBlock,viewModel: DraggableViewModel, deltaWidth: Dp = 0.dp, deltaHeight: Dp = 0.dp, isPositive: Boolean = true){ // Вызывается рекурсивно из верхней
        // перегрузки для пересчета размеров родительских блоков

        val parent = child.connectedParent

        if(parent == null) return

        val plusOrMinus = if(isPositive) 1 else -1

        val connectionView = child.connectedParentConnectionView!!.copy()

        if(parent.isInner && connectionView.extendType == ExtendConnectionViewType.NONE){

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
            changeParentParams(currentBlock,viewModel, deltaWidth, deltaHeight, isPositive = isPositive)

            if(!isPositive)ConnectorManager.normalizeConnectorsPositions(currentBlock,viewModel, besides = child)
            else ConnectorManager.normalizeConnectorsPositions(currentBlock,viewModel)
            return
        }

        when(connectionView.extendType){
            ExtendConnectionViewType.SIDE -> {

                val mxHeight = getTheMostHeightWithoutChild(parent, child)
                if(mxHeight <= child.height.value - deltaHeight * plusOrMinus){
                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionY > connectionView.positionY){
                            connection.positionY += deltaHeight * plusOrMinus
                        }
                        else if(connection.positionY == connectionView.positionY){
                            connection.positionY += deltaHeight/2 * plusOrMinus
                        }
                    }
                    if(parent.outputConnectionView!!.positionY == connectionView.positionY){
                        parent.outputConnectionView!!.positionY += deltaHeight/2 * plusOrMinus
                    }
                    parent.height.value += deltaHeight * plusOrMinus

                    changeParentParams(parent,viewModel, deltaHeight = deltaHeight, isPositive = isPositive)
                }
            }
            ExtendConnectionViewType.INNER -> {

                val mxHeight = getTheMostHeightWithoutChild(parent, child)

                if(mxHeight <= child.height.value - deltaHeight* plusOrMinus){
                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionY > connectionView.positionY){
                            connection.positionY += deltaHeight * plusOrMinus
                        }
                        else if(connection.positionY == connectionView.positionY){
                            connection.positionY += deltaHeight/2 * plusOrMinus
                        }
                    }
                    if(parent.outputConnectionView!!.positionY == connectionView.positionY){
                        parent.outputConnectionView!!.positionY += deltaHeight/2 * plusOrMinus
                    }
                    parent.height.value += deltaHeight * plusOrMinus
                }
                if(child.connectedParentConnectionView!!.width <= child.width.value - deltaWidth * plusOrMinus){
                    parent.inputConnectionViews.forEach{ connection ->
                        if(connection.positionX > connectionView.positionX){
                            connection.positionX += deltaWidth * plusOrMinus
                        }
                    }
                    parent.width.value += deltaWidth * plusOrMinus
                }

                changeParentParams(parent,viewModel, deltaWidth, deltaHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.INNER_BOTTOM -> {
                parent.inputConnectionViews.forEach{ connection ->
                    if(connection.positionY > connectionView.positionY){
                        connection.positionY += deltaHeight * plusOrMinus
                    }
                }
                parent.height.value += deltaHeight * plusOrMinus
                changeParentParams(parent,viewModel, deltaHeight = deltaHeight, isPositive = isPositive)
            }
            ExtendConnectionViewType.NONE -> {}
        }
        if(!isPositive)ConnectorManager.normalizeConnectorsPositions(parent,viewModel, besides = child)
        else ConnectorManager.normalizeConnectorsPositions(parent,viewModel)
    }
}