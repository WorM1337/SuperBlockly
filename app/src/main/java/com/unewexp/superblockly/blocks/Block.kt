package com.example.myfirstapplicatioin.blocks


import com.example.myfirstapplicatioin.model.BlockType
import java.util.UUID


// это класс описывает функционал блока
abstract class Block(val id: UUID, val blockType: BlockType) {
    abstract fun execution(context: ExecutionContext);
}