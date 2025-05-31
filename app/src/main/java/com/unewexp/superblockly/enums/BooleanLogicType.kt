package com.unewexp.superblockly.enums

enum class BooleanLogicType {
    AND,
    OR
}

fun BooleanLogicType.symbol(): String {
    return when (this) {
        BooleanLogicType.AND -> "&&"
        BooleanLogicType.OR -> "||"
    }
}