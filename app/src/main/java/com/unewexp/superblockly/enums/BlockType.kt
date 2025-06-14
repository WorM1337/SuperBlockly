package com.unewexp.superblockly.enums

enum class BlockType{

    SET_VARIABLE_VALUE,
    START,

    INT_LITERAL,
    STRING_LITERAL,
    BOOLEAN_LITERAL,

    OPERAND,
    SHORTHAND_ARITHMETIC_BLOCK,

    VARIABLE_DECLARATION,
    VARIABLE_REFERENCE,
    STRING_CONCAT,
    STRING_APPEND,
    PRINT_BLOCK,

    COMPARE_NUMBERS_BLOCK,
    BOOLEAN_LOGIC_BLOCK,
    NOT_BLOCK,

    IF_BLOCK,
    ELSE_BLOCK,
    IF_ELSE_BLOCK,

    REPEAT_N_TIMES,
    WHILE_BLOCK,
    FOR_BLOCK,
    FOR_ELEMENT_IN_LIST,

    FIXED_VALUE_AND_SIZE_LIST,
    GET_VALUE_BY_INDEX,
    REMOVE_VALUE_BY_INDEX,
    ADD_VALUE_BY_INDEX,
    GET_LIST_SIZE,
    EDIT_VALUE_BY_INDEX,
    PUSH_BACK_ELEMENT,

}