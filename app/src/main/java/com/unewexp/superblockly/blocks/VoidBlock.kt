package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.BlockType
import java.util.UUID

abstract class VoidBlock() : Block(UUID.randomUUID(), BlockType.NONE) {}