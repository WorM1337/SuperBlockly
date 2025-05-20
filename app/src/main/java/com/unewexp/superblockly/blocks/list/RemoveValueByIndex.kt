package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class RemoveValueByIndex : VoidBlock(UUID.randomUUID(), BlockType.REMOVE_VALUE_BY_INDEX) {

    val listConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.FIXED_VALUE_AND_SIZE_LIST
        )
    )

    val idConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    @Suppress("UNCHECKED_CAST")
    override fun execute() {
        val list = listConnector.connectedTo?.evaluate() as? MutableList<*>
            ?: throw IllegalStateException("Переданная переменная не содержит список")

        val index = idConnector.connectedTo?.evaluate() as? Int
            ?: throw java.lang.IllegalStateException("Индекс должен быть типа Int")

        if (index !in list.indices){
            throw IllegalStateException("Индекс $index выходит за пределы списка (0..${list.size - 1}")
        }
        (list as MutableList<Any?>).removeAt(index)
    }

}