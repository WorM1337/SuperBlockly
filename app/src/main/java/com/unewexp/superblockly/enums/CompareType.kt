package com.unewexp.superblockly.enums

enum class CompareType {
    GREATER_EQUAL,
    LESS_EQUAL,
    EQUAL,
    NOT_EQUAL,
    GREATER,
    LESS
}

fun CompareType.symbol(): String {
    return when (this) {
        CompareType.GREATER_EQUAL -> ">="
        CompareType.LESS_EQUAL -> "<="
        CompareType.EQUAL -> "=="
        CompareType.NOT_EQUAL -> "!="
        CompareType.GREATER -> ">"
        CompareType.LESS -> "<"
    }
}