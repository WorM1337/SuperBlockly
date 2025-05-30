package com.unewexp.superblockly.model

import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.model.ConnectionView
import com.example.myfirstapplicatioin.utils.disconnect
import com.example.myfirstapplicatioin.utils.safeConnect
import com.unewexp.superblockly.DraggableBlock
import com.unewexp.superblockly.DraggableViewModel
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.arithmetic.OperandBlock
import com.unewexp.superblockly.blocks.arithmetic.ShorthandArithmeticOperatorBlock
import com.unewexp.superblockly.blocks.list.AddElementByIndex
import com.unewexp.superblockly.blocks.list.EditValueByIndex
import com.unewexp.superblockly.blocks.list.FixedValuesAndSizeList
import com.unewexp.superblockly.blocks.list.GetListSize
import com.unewexp.superblockly.blocks.list.GetValueByIndex
import com.unewexp.superblockly.blocks.list.PushBackElement
import com.unewexp.superblockly.blocks.list.RemoveValueByIndex
import com.unewexp.superblockly.blocks.literals.BooleanLiteralBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.BooleanLogicBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.blocks.logic.ElseBlock
import com.unewexp.superblockly.blocks.logic.ElseIfBlock
import com.unewexp.superblockly.blocks.logic.IfBlock
import com.unewexp.superblockly.blocks.logic.NotBlock
import com.unewexp.superblockly.blocks.loops.ForBlock
import com.unewexp.superblockly.blocks.loops.ForElementInListBlock
import com.unewexp.superblockly.blocks.loops.RepeatNTimesBlock
import com.unewexp.superblockly.blocks.loops.WhileBlock
import com.unewexp.superblockly.blocks.returnBlocks.StringConcatenationBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.SetValueVariableBlock
import com.unewexp.superblockly.blocks.voidBlocks.StringAppendBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import com.unewexp.superblockly.enums.ExtendConnectionViewType
import com.unewexp.superblockly.viewBlocks.ViewInitialSize

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
    fun DisconnectBlock(sourceBlock: DraggableBlock, viewModel: DraggableViewModel){

        sourceBlock.connectedParent?.let{
            sourceBlock.connectedParentConnectionView?.let{
                disconnect(sourceBlock.outputConnectionView!!.connector, sourceBlock.connectedParentConnectionView!!.connector)

                SizeManager.changeParentParams(sourceBlock, viewModel, isPositive = false)

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

        viewModel.handleAction(DraggableViewModel.BlocklyAction.MoveBlock(
            connectable,
            target.x.value + nearestConX - (connectable.x.value + sourceConX),
            target.y.value + nearestConY - (connectable.y.value + sourceConY)
        ))

        connectable.connectedParent = target
        connectable.connectedParentConnectionView = connection
        target.scope.add(connectable)
        Log.i("Connect", "${connectable.block.blockType}")

        SizeManager.changeParentParams(connectable, viewModel)

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

        if(CalculationsManager.getLengthFromConnections(sourceDragBlock, nearestBlock, sourceDragBlock.outputConnectionView!!, nearestConnection, density) <= connetionLength){
            safeConnect(sourceDragBlock.outputConnectionView!!.connector, nearestConnection.connector)


            val sourceConX = with(density) {sourceDragBlock.outputConnectionView!!.positionX.toPx()}
            val sourceConY = with(density) {sourceDragBlock.outputConnectionView!!.positionY.toPx()}
            val nearestConX = with(density) {nearestConnection.positionX.toPx()}
            val nearestConY = with(density) {nearestConnection.positionY.toPx()}

            viewModel.handleAction(DraggableViewModel.BlocklyAction.MoveBlock(
                sourceDragBlock,
                nearestBlock.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                nearestBlock.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
            ))

            sourceDragBlock.connectedParent = nearestBlock
            sourceDragBlock.connectedParentConnectionView = nearestConnection
            nearestBlock.scope.add(sourceDragBlock)
            Log.i("Connect", "${sourceDragBlock.block.blockType}")

            SizeManager.changeParentParams(sourceDragBlock, viewModel)

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
            else if(CalculationsManager.getLengthFromConnections(sourceDragBlock, sourceDragBlock.connectedParent!!, sourceDragBlock.outputConnectionView!!, sourceDragBlock.connectedParentConnectionView!!, density) > connetionLength){
                DisconnectBlock(sourceDragBlock, viewModel)


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

                viewModel.handleAction(DraggableViewModel.BlocklyAction.MoveBlock(
                    sourceDragBlock,
                    sourceDragBlock.connectedParent!!.x.value + nearestConX - (sourceDragBlock.x.value + sourceConX),
                    sourceDragBlock.connectedParent!!.y.value + nearestConY - (sourceDragBlock.y.value + sourceConY)
                ))

                return false
            }
        }
        return true
    }

    fun normalizeConnectorsPositions(block: DraggableBlock, viewModel: DraggableViewModel, besides: DraggableBlock? = null){
        val density = viewModel.density
        block.scope.forEach{ child ->
            if(besides == null || child != besides) {
                val childConX = with(density) {child.outputConnectionView!!.positionX.toPx()}
                val childConY = with(density) {child.outputConnectionView!!.positionY.toPx()}
                val ConX = with(density) {child.connectedParentConnectionView!!.positionX.toPx()}
                val ConY = with(density) {child.connectedParentConnectionView!!.positionY.toPx()}

                viewModel.handleAction(DraggableViewModel.BlocklyAction.MoveBlock(
                    child,
                    child.connectedParent!!.x.value + ConX - (child.x.value + childConX),
                    child.connectedParent!!.y.value + ConY - (child.y.value + childConY)
                ))
            }
        }
    }

    private fun getBlockWithNearestConnection(dragBlock: DraggableBlock, listBlocks: MutableList<DraggableBlock>, density: Density, connectionTypes: MutableList<ConnectorType>, requiresVoid: Boolean): DraggableBlock? {

        var ans: DraggableBlock? = null

        var r = 1000000.0

        if(requiresVoid){
            for(block in listBlocks) {

                if(block.block !is VoidBlock) continue

                for(connection in block.inputConnectionViews){

                    if(connection.connector.connectionType !in connectionTypes || connection.isConnected) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                    val length = CalculationsManager.getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
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

                    val length = CalculationsManager.getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                    if( length < r){
                        r = length
                        ans = block
                    }
                }
            }
        }

        return ans
    }
    private fun getNearestConnection(dragBlock: DraggableBlock,listBlocks: MutableList<DraggableBlock>, density: Density, connectionTypes:MutableList<ConnectorType>): ConnectionView? {

        var ans: ConnectionView? = null

        var r = 1000000.0

        for(block in listBlocks) {

            for(connection in block.inputConnectionViews){

                if(connection.connector.connectionType !in connectionTypes || connection.isConnected) continue // Проверка на то, что мы ищем именно тот тип коннектора, который нам нужен

                val length = CalculationsManager.getLengthFromConnections(dragBlock, block, dragBlock.outputConnectionView!!, connection, density)
                if( length < r){
                    r = length
                    ans = connection
                }
            }
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
        val defaultHeight = ViewInitialSize.defaultHeight
        val defaultWidth = ViewInitialSize.defaultWidth
        val innerPadding = ViewInitialSize.defaultInnerPadding
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
                    ConnectionView(castedBlock.leftInputConnector, width/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                )
            }
            BlockType.SET_VARIABLE_VALUE -> {
                val castedBlock = (block as SetValueVariableBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.VARIABLE_DECLARATION -> {
                val castedBlock = (block as VariableDeclarationBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.valueInputConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
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
                    ConnectionView(castedBlock.leftInputConnector, width/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                )
            }
            BlockType.STRING_APPEND -> {
                val castedBlock = (block as StringAppendBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.PRINT_BLOCK -> {
                val castedBlock = (block as PrintBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.IF_BLOCK -> {
                val castedBlock = (block as IfBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.conditionConnector, width, defaultHeight/2, ExtendConnectionViewType.SIDE, height = defaultHeight)
                )
            }
            BlockType.FOR_BLOCK -> {
                val castedBlock = (block as ForBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.initialValueBlock, width/3 + innerPadding*3, defaultHeight/2, ExtendConnectionViewType.INNER, width = defaultWidth, height = defaultHeight-innerPadding*2),
                    ConnectionView(castedBlock.maxValueBlock, width*5/9+ innerPadding, defaultHeight/2, ExtendConnectionViewType.INNER, width = defaultWidth, height = defaultHeight-innerPadding*2),
                    ConnectionView(castedBlock.stepBlock, width*7/9+ innerPadding, defaultHeight/2, ExtendConnectionViewType.INNER, width = defaultWidth, height = defaultHeight-innerPadding*2)
                )
            }

            BlockType.SHORTHAND_ARITHMETIC_BLOCK -> {
                val castedBlock = (block as ShorthandArithmeticOperatorBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                )
            }
            BlockType.COMPARE_NUMBERS_BLOCK -> {
                val castedBlock = (block as CompareNumbers)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth + innerPadding*2),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4-defaultWidth/2-innerPadding*2, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth + innerPadding*2),
                )
            }
            BlockType.BOOLEAN_LOGIC_BLOCK -> {
                val castedBlock = (block as BooleanLogicBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.leftInputConnector, width/4, height/2, ExtendConnectionViewType.INNER),
                    ConnectionView(castedBlock.rightInputConnector, width*3/4, height/2, ExtendConnectionViewType.INNER),
                )
            }
            BlockType.NOT_BLOCK -> {
                val castedBlock = (block as NotBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.inputConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                )
            }
            BlockType.ELSE_BLOCK -> {
                val castedBlock = (block as ElseBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                )
            }
            BlockType.IF_ELSE_BLOCK -> {
                val castedBlock = (block as ElseIfBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.conditionConnector, width, defaultHeight/2, ExtendConnectionViewType.SIDE, height = defaultHeight)
                )
            }
            BlockType.REPEAT_N_TIMES -> {
                val castedBlock = (block as RepeatNTimesBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.countRepeatTimesConnector, width, defaultHeight/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                )
            }
            BlockType.WHILE_BLOCK -> {
                val castedBlock = (block as WhileBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.conditionConnector, width, defaultHeight/2, ExtendConnectionViewType.SIDE, height = defaultHeight-innerPadding*2)
                )
            }
            BlockType.FOR_ELEMENT_IN_LIST -> {
                val castedBlock = (block as ForElementInListBlock)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.innerConnector, cornerOffset*2, defaultHeight, ExtendConnectionViewType.INNER_BOTTOM),
                    ConnectionView(castedBlock.listConnector, width*3/4, defaultHeight, ExtendConnectionViewType.INNER),
                )
            }
            BlockType.FIXED_VALUE_AND_SIZE_LIST -> {
                val castedBlock = (block as FixedValuesAndSizeList)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.valueInput, width * 1/3, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.repeatTimes, width * 2/3-innerPadding, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                )
            }
            BlockType.GET_VALUE_BY_INDEX -> {
                val castedBlock = (block as GetValueByIndex)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.listConnector, width/4 + innerPadding, height/2, ExtendConnectionViewType.INNER, height= defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.idConnector, width*3/4, height/2, ExtendConnectionViewType.INNER, height= defaultHeight-innerPadding*2, width = defaultWidth/2)
                )
            }
            BlockType.REMOVE_VALUE_BY_INDEX -> {
                val castedBlock = (block as RemoveValueByIndex)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.listConnector, width/4, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.idConnector, width*3/4, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth)
                )
            }
            BlockType.ADD_VALUE_BY_INDEX -> {
                val castedBlock = (block as AddElementByIndex)

                ans += mutableListOf(
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.listConnector, width/4 - innerPadding, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.idConnector, width*3/5 - innerPadding, height/2, ExtendConnectionViewType.INNER, height = defaultHeight-innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.valueConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight)
                )
            }
            BlockType.GET_LIST_SIZE -> {
                val castedBlock = (block as GetListSize)

                ans += mutableListOf(
                    ConnectionView(castedBlock.outputConnector, 0.dp, height/2),
                    ConnectionView(castedBlock.listConnector, width, height/2, ExtendConnectionViewType.SIDE, height = defaultHeight),
                )
            }
            BlockType.EDIT_VALUE_BY_INDEX -> {
                val castedBlock = (block as EditValueByIndex)

                ans += mutableListOf(
                    ConnectionView(castedBlock.listConnector, width/5, height/2, extendType = ExtendConnectionViewType.INNER, height = defaultHeight - innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.idConnector, width/2+innerPadding, height/2, extendType = ExtendConnectionViewType.INNER, height = defaultHeight - innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.valueConnector, width, height/2, extendType = ExtendConnectionViewType.SIDE, height = defaultHeight)
                )
            }
            BlockType.PUSH_BACK_ELEMENT -> {
                val castedBlock = (block as PushBackElement)

                ans += mutableListOf(
                    ConnectionView(castedBlock.listConnector, width/3-innerPadding*2, height/2, extendType = ExtendConnectionViewType.INNER, height = defaultHeight - innerPadding*2, width = defaultWidth),
                    ConnectionView(castedBlock.topConnector, cornerOffset, 0.dp),
                    ConnectionView(castedBlock.bottomConnector, cornerOffset, height),
                    ConnectionView(castedBlock.valueConnector, width, height/2, extendType = ExtendConnectionViewType.SIDE, height = defaultHeight)
                )
            }
        }
        return ans
    }
}