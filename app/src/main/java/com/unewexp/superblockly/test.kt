package com.unewexp.superblockly

import com.example.myfirstapplicatioin.blocks.literals.IntLiteralBlock
import com.example.myfirstapplicatioin.utils.safeConnect
import com.unewexp.superblockly.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.StartBlock
import com.unewexp.superblockly.blocks.arithmetic.ShorthandArithmeticOperatorBlock
import com.unewexp.superblockly.blocks.literals.StringLiteralBlock
import com.unewexp.superblockly.blocks.logic.CompareNumbers
import com.unewexp.superblockly.blocks.logic.ConditionBlock
import com.unewexp.superblockly.blocks.loops.RepeatNTimesBlock
import com.unewexp.superblockly.blocks.loops.WhileBlock
import com.unewexp.superblockly.blocks.returnBlocks.VariableReferenceBlock
import com.unewexp.superblockly.blocks.voidBlocks.PrintBlock
import com.unewexp.superblockly.blocks.voidBlocks.VariableDeclarationBlock
import com.unewexp.superblockly.enums.CompareType

fun main() {
    val startBlock = StartBlock()
//
//    val int1 = IntLiteralBlock(13)
//    val int2 = IntLiteralBlock(14)
//    val int3 = IntLiteralBlock(15)
//    val int4 = IntLiteralBlock(15)
//    val boolLiteral = BooleanLiteralBlock(true)
//
//
//
    val printBlock1 = PrintBlock()
    val string1 = StringLiteralBlock("Начало программы")
    safeConnect(printBlock1.inputConnector, string1.outputConnector)
//
    val printBlock2 = PrintBlock()
    val string2 = StringLiteralBlock("If блок выполнился")
    safeConnect(printBlock2.inputConnector, string2.outputConnector)
//
//    val printBlock3 = PrintBlock()
//    val string3 = StringLiteralBlock("Else if блок выполнился")
//    safeConnect(printBlock3.inputConnector, string3.outputConnector)
//
//    val printBlock4 = PrintBlock()
//    val string4 = StringLiteralBlock("Else блок выполнился")
//    safeConnect(printBlock4.inputConnector, string4.outputConnector)
//
//    val printBlock5 = PrintBlock()
//    val string5 = StringLiteralBlock("Последний блок выполнился")
//    safeConnect(printBlock5.inputConnector, string5.outputConnector)
//
//
//
//    val compareNumbers1 = CompareNumbers()
//    safeConnect(compareNumbers1.leftInputConnector, int1.outputConnector)
//    safeConnect(compareNumbers1.rightInputConnector, int2.outputConnector)
//    compareNumbers1.compareType = CompareType.GREATER
//
//    val compareNumbers2 = CompareNumbers()
//    safeConnect(compareNumbers2.leftInputConnector, int3.outputConnector)
//    safeConnect(compareNumbers2.rightInputConnector, int4.outputConnector)
//    compareNumbers2.compareType = CompareType.GREATER_EQUAL
//
//
//    val if1 = IfBlock()
//    safeConnect(if1.conditionConnector, compareNumbers1.outputConnector)
//    safeConnect(if1.innerConnector, printBlock2.topConnector)
//
//    val elseif = ElseIfBlock()
//    safeConnect(elseif.conditionConnector, compareNumbers2.outputConnector)
//    safeConnect(elseif.innerConnector, printBlock3.topConnector)
//
//    val elseBlock = ElseBlock()
//    safeConnect(elseBlock.innerConnector, printBlock4.topConnector)
//
//    safeConnect(startBlock.bottomConnector, printBlock1.topConnector)
//    safeConnect(printBlock1.bottomConnector, if1.topConnector)
//    safeConnect(if1.bottomConnector, elseif.topConnector)
//    safeConnect(elseif.bottomConnector, elseBlock.topConnector)
//    safeConnect(elseBlock.bottomConnector, printBlock5.topConnector)

//    val loop = RepeatNTimesBlock()
//    safeConnect(startBlock.bottomConnector, loop.topConnector)
//    safeConnect(loop.innerConnector, printBlock1.topConnector)
//    safeConnect(printBlock1.bottomConnector, printBlock2.topConnector)

    val variableCount = VariableDeclarationBlock("x")

    val variableReference = VariableReferenceBlock()
    variableReference.selectedVariableName = "x"

    val variableReference2 = VariableReferenceBlock()
    variableReference2.selectedVariableName = "x"


    val currentValue = IntLiteralBlock(0)
    val next = IntLiteralBlock(1)
    val maxvalue = IntLiteralBlock(15)

    safeConnect(variableCount.valueInputConnector, currentValue.outputConnector)

    val printBlock = PrintBlock()
    safeConnect(printBlock.inputConnector, variableReference.outputConnector)

    val condition = CompareNumbers()
    safeConnect(condition.leftInputConnector, variableReference2.outputConnector)
    safeConnect(condition.rightInputConnector, maxvalue.outputConnector)
    condition.compareType = CompareType.LESS


    val shortOperand = ShorthandArithmeticOperatorBlock()
    shortOperand.selectedVariableName = "x"
    safeConnect(shortOperand.inputConnector, next.outputConnector)


    val whileCycle = WhileBlock()
    safeConnect(whileCycle.conditionConnector, condition.outputConnector)
    safeConnect(whileCycle.innerConnector, printBlock.topConnector)
    safeConnect(printBlock.bottomConnector, shortOperand.topConnector)



    safeConnect(startBlock.bottomConnector, variableCount.topConnector)
    safeConnect(variableCount.bottomConnector, whileCycle.topConnector)




    // вывести все логи в консоль
    startBlock.execute()
    val logs = ExecutionContext.logs
    for (log in logs){
        println(log)
    }

}