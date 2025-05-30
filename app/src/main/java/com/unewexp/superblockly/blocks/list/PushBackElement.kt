package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

class PushBackElement: VoidBlock(UUID.randomUUID(), blockType = BlockType.PUSH_BACK_ELEMENT) {
    val listConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedBlockTypes = setOf(
            BlockType.VARIABLE_REFERENCE,
            BlockType.FIXED_VALUE_AND_SIZE_LIST
        )
    )

    val valueConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java, String::class.java, Boolean::class.java)
    )

    override suspend  fun execute() {
        checkDebugPause()

        val list = listConnector.connectedTo?.evaluate() as? MutableList<*>
            ?: throw BlockIllegalStateException(this, "Переданная переменная не содержит список")


        val value = valueConnector.connectedTo?.evaluate()
            ?: throw BlockIllegalStateException(this, "Не указано значение для добавления")


        val listElementType = list.firstOrNull()?.javaClass
        val newValueType = value.javaClass

        if (newValueType != listElementType){
            throw BlockIllegalStateException(this, "Тип элемента списка (${listElementType?.simpleName}) " +
                    "не совпадает с типом добавляемого элемента (${newValueType.simpleName})")
        }

        @Suppress("UNCHECKED_CAST")
        val mutableList = list as MutableList<Any?>

        mutableList.add(value)

    }
}