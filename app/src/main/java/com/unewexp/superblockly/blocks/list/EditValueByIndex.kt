package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class EditValueByIndex : VoidBlock(UUID.randomUUID(), BlockType.EDIT_VALUE_BY_INDEX) {

    val listConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE
        )
    )

    val idConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val valueConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(
            String::class.java,
            Int::class.java,
            Boolean::class.java
        )
    )

    override suspend fun execute() {
        checkDebugPause()

        val list = listConnector.connectedTo?.evaluate() as? MutableList<*>
            ?: throw IllegalStateException("Переданная переменная не содержит список")

        val index = idConnector.connectedTo?.evaluate() as? Int
            ?: throw java.lang.IllegalStateException("Индекс должен быть типа Int")

        val newValue = valueConnector.connectedTo?.evaluate()
            ?: throw java.lang.IllegalStateException("Не указано значение для добавления")


        val listElementType = list.firstOrNull()?.javaClass
        val newValueType = newValue.javaClass

        if (newValueType != listElementType){
            throw IllegalStateException("Тип элемента списка (${listElementType?.simpleName}) " +
                    "не совпадает с типом добавляемого элемента (${newValueType.simpleName})")
        }

        @Suppress("UNCHECKED_CAST")
        val mutableList = list as MutableList<Any?>

        if (index !in 0..mutableList.size){
            throw IllegalStateException("Индекс массива не существует: $index")
        }

        mutableList[index] = newValue

    }

}