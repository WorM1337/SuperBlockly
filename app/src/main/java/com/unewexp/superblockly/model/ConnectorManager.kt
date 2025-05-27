package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.utils.disconnect
import com.example.myfirstapplicatioin.utils.safeConnect
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.IfBlock
import com.unewexp.superblockly.blocks.returnBlocks.StringConcatenationBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.StringAppendBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.blocks.list.FixedValuesAndSizeList
import com.unewexp.superblockly.blocks.loops.ForBlock
import com.unewexp.superblockly.enums.ExtendConnectionViewType
import com.unewexp.superblockly.viewBlocks.ViewInitialSize
import kotlin.math.pow
import kotlin.math.sqrt

object ConnectorManager {
    val connetionLength = 70.0


    fun tryConnectBlocks(connectable: DraggableBlock, target: DraggableBlock, connection: ConnectionView, viewModel: DraggableViewModel, density: Density) : Boolean{
        when(connectable.outputConnectionView!!.connector.connectionType){
            ConnectorType.OUTPUT -> {
                if(connection.connector.connectionType !=ConnectorType.INPUT) return false

                connectBlocks(connectable, target, connection, viewModel, density)
                return true
            }
            ConnectorType.STRING_TOP -> {
                if(connection.connector.connectionType !=ConnectorType.STRING_BOTTOM_INNER && connection.connector.connectionType !=ConnectorType.STRING_BOTTOM_OUTER) return false

                connectBlocks(connectable, target, connection, viewModel, density)
                return true
            }
            else -> throw IllegalArgumentException("Коннектор типа ${connectable.outputConnectionView!!.connector.connectionType} выступил как получатель")
        }
    }
    fun DisconnectBlock(sourceBlock: DraggableBlock){

        sourceBlock.connectedParent?.let{
            sourceBlock.connectedParentConnectionView?.let{
                disconnect(sourceBlock.outputConnectionView!!.connector, sourceBlock.connectedParentConnectionView!!.connector)

                changeParentParams(sourceBlock, isPositive = false)

                sourceBlock.connectedParent!!.scope.remove(sourceBlock)

                sourceBlock.connectedParentConnectionView!!.isConnected = false
                sourceBlock.outputConnectionView!!.isConnected = false

                sourceBlock.connectedParent = null
                sourceBlock.connectedParentConnectionView = null
                Log.i("Disconnect", "${sourceBlock.block.blockType}")
            }
        }



    }
    fun tryConnectAndDisconnectDrag(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density){

        if(!parentDisconnectOrNot(sourceDragBlock, viewModel, density)) return // Рассконекчивает, если достаточно далеко

        if(sourceDragBlock.connectedParent!= null && sourceDragBlock.connectedParentConnectionView != null) return; // Если не расконектил, то не надо и коннектить


        tryConnectBlockOnDistance(sourceDragBlock, viewModel, density, sourceDragBlock.outputConnectionView!!.connector.connectionType)
    }

    private fun connectBlocks(connectable: DraggableBlock, target: DraggableBlock, connection: ConnectionView, viewModel: DraggableViewModel, density: Density){
        safeConnect(connectable.outputConnectionView!!.connector, connection.connector)


        val sourceConX = with(density) {connectable.outputConnectionView!!.positionX.toPx()}
        val sourceConY = with(density) {connectable.outputConnectionView!!.positionY.toPx()}
        val nearestConX = with(density) {connection.positionX.toPx()}
        val nearestConY = with(density) {connection.positionY.toPx()}

        viewModel.updateBlockPosition(
            connectable,
            target.x.value + nearestConX - (connectable.x.value + sourceConX),
            target.y.value + nearestConY - (connectable.y.value + sourceConY)
        )
        connectable.connectedParent = target
        connectable.connectedParentConnectionView = connection
        target.scope.add(connectable)
        Log.i("Connect", "${connectable.block.blockType}")

        changeParentParams(connectable)

        // Для всех дочерних блоков нужно высчитать isInner. Для коннекторов кроме STRING_BOTTOM_OUTER isInner наследуется
        // (если после этого встретили OUTER, то его помечаем isInner для корректного удаления)
        if(connectable.connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_OUTER || connectable.connectedParent!!.isInner){
            val stack: MutableList<DraggableBlock> = mutableListOf(connectable)

            while(!stack.isEmpty()){
                val current = stack.removeAt(stack.size-1)

                current.isInner = true
                for(child in current.scope){
                    if(!child.isInner) stack.add(child)
                }
            }
        }
        else{
            val stack: MutableList<DraggableBlock> = mutableListOf(connectable)

            while(!stack.isEmpty()){
                val current = stack.removeAt(stack.size-1)

                current.isInner = false
                for(child in current.scope){
                    if(child.connectedParentConnectionView!!.connector.connectionType == ConnectorType.STRING_BOTTOM_OUTER) stack.add(child)
                }
            }

        }
        connectable.connectedParentConnectionView!!.isConnected = true
        connectable.outputConnectionView!!.isConnected = true
    }

    private fun tryConnectBlockOnDistance(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density, connectionType: ConnectorType){

        val listDragBlocks = viewModel.blocks.value.filter { it !in sourceDragBlock.scope && (it != sourceDragBlock) }.toMutableList()

        val nearestBlock: DraggableBlock?
        val nearestConnection: ConnectionView?

        when(connectionType){
            ConnectorType.OUTPUT -> {
                nearestBlock = getBlockWithNearestConnection(sourceDragBlock, listDragBlocks, density,
                    mutableListOf(ConnectorType.INPUT), false)
                nearestConnection = getNearestConnection(sourceDragBlock, listDragBlocks, density,
                    mutableListOf(ConnectorType.INPUT))
            }
            ConnectorType.STRING_TOP -> {
                nearestBlock = getBlockWithNearestConnection(sourceDragBlock, listDragBlocks, density,
                    mutableListOf(ConnectorType.STRING_BOTTOM_INNER, ConnectorType.STRING_BOTTOM_OUTER), true)
                nearestConnection = getNearestConnection(sourceDragBlock, listDragBlocks, density,
                    mutableListOf(ConnectorType.STRING_BOTTOM_INNER, ConnectorType.STRING_BOTTOM_OUTER))

            }
            else -> throw IllegalArgumentException("Коннектор типа ${sourceDragBlock.outputConnectionView!!.connector.connectionType} выступил как получатель")
        }


        if(nearestBlock == null || sourceDragBlock.outputConnectionView == null || nearestConnection == null) return

        if(getLengthFromConnections(sourceDragBlock, nearestBlock, sourceDragBlock.outputConnectionView!!, nearestConnection, density) <= connetionLength){
            safeConnect(sourceDragBlock.outputConnectionView!!.connector, nearestConnection.connector)


            val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
            val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
            val nearestConX = with(density) {nearestConnection.positionX.toPx()}
            val nearestConY = with(density) {nearestConnection.positionY.toPx()}

            viewModel.updateBlockPosition(
                sourceDragBlock,
                nearestBlock.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                nearestBlock.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
            )
            sourceDragBlock.connectedParent = nearestBlock
            sourceDragBlock.connectedParentConnectionView = nearestConnection
            nearestBlock.scope.add(sourceDragBlock)
            Log.i("Connect", "${sourceDragBlock.block.blockType}")

            changeParentParams(sourceDragBlock)

            // Для всех дочерних блоков нужно высчитать isInner. Для коннекторов кроме STRING_BOTTOM_OUTER isInner наследуется
            // (если после этого встретили OUTER, то его помечаем isInner для корректного удаления)
            if(sourceDragBlock.connectedParentConnectionView!!.connector.connectionType != ConnectorType.STRING_BOTTOM_OUTER || sourceDragBlock.connectedParent!!.isInner){
                val stack: MutableList<DraggableBlock> = mutableListOf(sourceDragBlock)

                while(!stack.isEmpty()){
                    val current = stack.removeAt(stack.size-1)

                    current.isInner = true
                    for(child in current.scope){
                        if(!child.isInner) stack.add(child)
                    }
                }
            }
            else{
                val stack: MutableList<DraggableBlock> = mutableListOf(sourceDragBlock)

                while(!stack.isEmpty()){
                    val current = stack.removeAt(stack.size-1)

                    current.isInner = false
                    for(child in current.scope){
                        if(child.connectedParentConnectionView!!.connector.connectionType == ConnectorType.STRING_BOTTOM_OUTER) stack.add(child)
                    }
                }

            }
            sourceDragBlock.connectedParentConnectionView!!.isConnected = true
            sourceDragBlock.outputConnectionView!!.isConnected = true
        }
    }



    private fun parentDisconnectOrNot(sourceDragBlock: DraggableBlock, viewModel: DraggableViewModel, density: Density): Boolean {
        sourceDragBlock.connectedParent?.let {
            if(sourceDragBlock.connectedParentConnectionView == null) {
                sourceDragBlock.connectedParent = null
                throw IllegalArgumentException("ConnectionView для ${sourceDragBlock.block.blockType} оказался null")
            }
            else if(getLengthFromConnections(sourceDragBlock, sourceDragBlock.connectedParent!!, sourceDragBlock.outputConnectionView!!, sourceDragBlock.connectedParentConnectionView!!, density) > connetionLength){
                DisconnectBlock(sourceDragBlock)

                // Убираем isInner для всех прикреплённых нижних блоков

                val stack: MutableList<DraggableBlock> = mutableListOf(sourceDragBlock)

                while(!stack.isEmpty()){
                    val current = stack.removeAt(stack.size-1)

                    current.isInner = false
                    for(child in current.scope){
                        if(child.connectedParentConnectionView!!.connector.connectionType == ConnectorType.STRING_BOTTOM_OUTER) stack.add(child)
                    }
                }

                return true
            }
            else{

                val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
                val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
                val nearestConX = with(density) {sourceDragBlock.connectedParentConnectionView!!.positionX.toPx()}
                val nearestConY = with(density) {sourceDragBlock.connectedParentConnectionView!!.positionY.toPx()}

                viewModel.updateBlockPosition(
                    sourceDragBlock,
                    sourceDragBlock.connectedParent!!.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                    sourceDragBlock.connectedParent!!.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
                )

                return false
            }
        }
        return true
    }

    private fun getLengthFromConnections(
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
    fun getBlockWithNearestConnection(dragBlock: DraggableBlock, listBlocks: MutableList<DraggableBlock>, density: Density, connectionTypes: MutableList<ConnectorType>, requiresVoid: Boolean): DraggableBlock? {

        var ans: DraggableBlock? = null

        var r = 1000000.0

        if(requiresVoid){
            for(block in listBlocks) {

                if(block.block !is VoidBlock) continue

                for(connection in block.inputConnectionViews){

                    if(connection.connector.connectionType !in connectionTypes || connection.isConnected) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                    val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                    if( length < r){
                        r = length
                        ans = block
                    }
                }

            }
        }
        else {
            for(block in listBlocks) {

                for(connection in block.inputConnectionViews ){

                    if(connection.connector.connectionType !in connectionTypes || connection.isConnected) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                    val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                    if( length < r){
                        r = length
                        ans = block
                    }
                }
            }
        }

        return ans
    }
    fun getNearestConnection(dragBlock: DraggableBlock,listBlocks: MutableList<DraggableBlock>, density: Density, connectionTypes:MutableList<ConnectorType>): ConnectionView? {

        var ans: ConnectionView? = null

        var r = 1000000.0

        for(block in listBlocks) {

            for(connection in block.inputConnectionViews){

                if(connection.connector.connectionType !in connectionTypes || connection.isConnected) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                val length = getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                if( length < r){
                    r = length
                    ans = connection
                }
            }
        }
        return ans
    }


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
            val deltaHeight = getSummaryHeight(child)
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
                val deltaHeight = getSummaryHeight(child)
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

    fun getSummaryHeight(block: DraggableBlock): Dp{
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

    fun getStringBottomOuterConnectionChild(dragBlock: DraggableBlock): DraggableBlock? {

        dragBlock.scope.forEach{
            if(it.connectedParentConnectionView!!.connector.connectionType == ConnectorType.STRING_BOTTOM_OUTER){
                return it
            }
        }
        return null
    }

    fun initConnectionsFromBlock(block: Block): MutableList<ConnectionView> {

        val ans: MutableList<ConnectionView> = mutableListOf()

        val cornerOffset = ViewInitialSize.cornerOffset

        val sizeOfBlock = ViewInitialSize.getInitialSizeByBlockType(block.blockType)
            ?: throw IllegalArgumentException("Для блока типа ${block.blockType} не заданы размеры")

        val width = sizeOfBlock.width
        val height = sizeOfBlock.height

        when(block.blockType) {
            BlockType.START -> {
                val castedBlock = (block as StartBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp)
                )
            }
            BlockType.OPERAND -> {
                val castedBlock = (block as OperandBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2, ExtendConnectionViewType.INNER),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2, ExtendConnectionViewType.INNER),
                )
            }
            BlockType.SET_VARIABLE_VALUE -> {
                val castedBlock = (block as SetValueVariableBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueConnector, width, height/2, ExtendConnectionViewType.SIDE),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.VARIABLE_DECLARATION -> {
                val castedBlock = (block as VariableDeclarationBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueInputConnector, width, height/2, ExtendConnectionViewType.SIDE),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.INT_LITERAL -> {
                val castedBlock = (block as IntLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_LITERAL -> {
                val castedBlock = (block as StringLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.BOOLEAN_LITERAL -> {
                val castedBlock = (block as BooleanLiteralBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.VARIABLE_REFERENCE -> {
                val castedBlock = (block as VariableReferenceBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                )
            }
            BlockType.STRING_CONCAT -> {
                val castedBlock = (block as StringConcatenationBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2, ExtendConnectionViewType.INNER),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2, ExtendConnectionViewType.INNER),
                )
            }
            BlockType.STRING_APPEND -> {
                val castedBlock = (block as StringAppendBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.PRINT_BLOCK -> {
                val castedBlock = (block as PrintBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.IF_BLOCK -> {
                val castedBlock = (block as IfBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset, 60.dp, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.conditionConnector, width, height/2, ExtendConnectionViewType.SIDE)
                )
            }
            BlockType.FOR_BLOCK -> {
                val castedBlock = (block as ForBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset, 60.dp, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.initialValueBlock, 150.dp, 10.dp),
                    ConnectionView(castedBlock.maxValueBlock, 225.dp, 10.dp),
                    ConnectionView(castedBlock.stepBlock, 320.dp, 10.dp)
                )
            }

            BlockType.SHORTHAND_ARITHMETIC_BLOCK -> TODO()
            BlockType.COMPARE_NUMBERS_BLOCK -> TODO()
            BlockType.BOOLEAN_LOGIC_BLOCK -> TODO()
            BlockType.NOT_BLOCK -> TODO()
            BlockType.ELSE_BLOCK -> TODO()
            BlockType.IF_ELSE_BLOCK -> TODO()
            BlockType.REPEAT_N_TIMES -> TODO()
            BlockType.WHILE_BLOCK -> TODO()
            BlockType.FOR_ELEMENT_IN_LIST -> TODO()
            BlockType.FIXED_VALUE_AND_SIZE_LIST -> {
                val castedBlock = (block as FixedValuesAndSizeList)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.valueInput, 100.dp, height/6),
                    ConnectionView(castedBlock.repeatTimes, 220.dp, height/6),
                )
            }
            BlockType.GET_VALUE_BY_INDEX -> TODO()
            BlockType.REMOVE_VALUE_BY_INDEX -> TODO()
            BlockType.ADD_VALUE_BY_INDEX -> TODO()
            BlockType.GET_LIST_SIZE -> TODO()
        }
        return ans
    }
}