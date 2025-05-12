package com.unewexp.superblockly.blocks.literals

import com.example.myfirstapplicatioin.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.ReturnBlock
import java.lang.IllegalArgumentException

class NumberBlock(var num: Int? = null) : ReturnBlock() {

    fun setNumber(text : String) {

        var n = text.toIntOrNull();

        n?.let {
            num = n;
            return;
        }

        throw IllegalArgumentException("Неверный ввод числа!")
    }

    fun getReturn(): Int? {
        return num
    }
    override fun execution(context: ExecutionContext) {
        // Добавить в node дерева инфу о блоке
    }
    override fun getInformationForTree() = null;
}