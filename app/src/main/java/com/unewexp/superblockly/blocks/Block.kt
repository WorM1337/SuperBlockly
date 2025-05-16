package com.example.myfirstapplicatioin.blocks



import com.unewexp.superblockly.enums.BlockType
import java.util.UUID


// это класс описывает функционал блока
abstract class Block(val id: UUID, val blockType: BlockType) {

    // Возвращает значение
    // Для выражений
    open fun evaluate(): Any? {
        throw UnsupportedOperationException("Этот блок не поддерживает evaluate()")
    }

    // Для инструкций
    open fun execute() {
        throw UnsupportedOperationException("Этот блок не поддерживает execute()")
    }

}