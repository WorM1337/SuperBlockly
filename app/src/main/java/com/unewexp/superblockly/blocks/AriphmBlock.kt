package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.blocks.ExecutionContext
import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.model.ConnectorType
import com.unewexp.superblockly.blocks.literals.NumberBlock


class AriphmBlock(val ariphmType: AriphmType) : ReturnBlock() {

    var conLeft = Connector(ConnectorType.INPUT, this)
    var conRight = Connector(ConnectorType.INPUT, this)

    fun getReturn(): Int? {

        var leftParam: Int? = (conLeft.connectedTo as AriphmBlock)?.getReturn()
        var rightParam: Int? = (conRight.connectedTo as AriphmBlock)?.getReturn()

        if(leftParam == null) {
            leftParam = (conLeft.connectedTo as NumberBlock)?.getReturn()
        }

        if(rightParam == null) {
            rightParam = (conLeft.connectedTo as NumberBlock)?.getReturn()
        }

        if(leftParam == null || rightParam == null) {
            throw IllegalArgumentException("В блок в качестве аргумента помещен не AriphBlock(NumberBlock)!")
        }

        return when(ariphmType) {
            AriphmType.PLUS -> leftParam + rightParam;
            AriphmType.MINUS -> leftParam - rightParam;
            AriphmType.MULTIPLICATION -> leftParam * rightParam;
            AriphmType.DVISION -> leftParam / rightParam;
            AriphmType.REMAINDER -> leftParam % rightParam;
        }

    }

    override fun getInformationForTree(): MutableList<Block>? {
        var leftParam: Block? = (conLeft.connectedTo as? AriphmBlock)
        var rightParam: Block? = (conRight.connectedTo as? AriphmBlock)

        if(leftParam == null) {
            leftParam = (conLeft.connectedTo as? NumberBlock)
        }

        if(rightParam == null) {
            rightParam = (conLeft.connectedTo as? NumberBlock)
        }

        if(leftParam == null || rightParam == null) {
            throw IllegalArgumentException("В блок в качестве аргумента помещен не AriphBlock(NumberBlock)!")
        }

        return mutableListOf(leftParam, rightParam)
    }

    override fun execution(context: ExecutionContext) {}
}