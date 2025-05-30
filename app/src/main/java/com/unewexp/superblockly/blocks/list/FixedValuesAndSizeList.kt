package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
import java.lang.IllegalStateException
import java.util.UUID

class FixedValuesAndSizeList : ListBlock(UUID.randomUUID(), BlockType.FIXED_VALUE_AND_SIZE_LIST) {

    val valueInput = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(
            Int::class.java,
            String::class.java,
            Boolean::class.java
        )
    )

    val repeatTimes = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    override suspend  fun evaluate(): Any? {
        checkDebugPause()

        val valueRepeat = valueInput.connectedTo?.evaluate()
            ?: throw IllegalStateException("Вы не указали значение для создания списка")

        val countRepeat = repeatTimes.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Количество элементов в списке должно быть типа Int")

        if (countRepeat < 0){
            throw IllegalStateException("Количество элементов не может быть отрицательным")
        }

        return MutableList(countRepeat) { valueRepeat }
    }
}