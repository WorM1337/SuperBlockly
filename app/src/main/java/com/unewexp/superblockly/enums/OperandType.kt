package com.unewexp.superblockly.enums


enum class OperandType{
    PLUS,
    MINUS,
    DIVISION,
    MULTIPLICATION,
    MODULO,
}

fun OperandType.symbol(): String {
    return when (this) {
        OperandType.PLUS -> "+"
        OperandType.MINUS -> "-"
        OperandType.DIVISION -> "/"
        OperandType.MULTIPLICATION -> "*"
        OperandType.MODULO -> "%"
    }
}