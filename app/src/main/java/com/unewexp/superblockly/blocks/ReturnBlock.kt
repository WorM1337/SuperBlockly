package com.unewexp.superblockly.blocks

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.BlockType
import com.example.myfirstapplicatioin.model.Connector
import java.util.UUID

abstract class ReturnBlock() : Block(UUID.randomUUID(), BlockType.NONE) {}

