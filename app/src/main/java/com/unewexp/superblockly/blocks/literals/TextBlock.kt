package com.unewexp.superblockly.blocks.literals

import com.example.myfirstapplicatioin.blocks.ExecutionContext
import com.unewexp.superblockly.blocks.ReturnBlock

class TextBlock(var text: String? = null) : ReturnBlock() {

    fun setField(text : String) {
        if(text.length != 0) this.text = text
    }

    fun getReturn(): String? {
        return text
    }

    override fun execution(context: ExecutionContext) {
        // Добавить в node дерева инфу о блоке
    }
    override fun getInformationForTree() = null;
}