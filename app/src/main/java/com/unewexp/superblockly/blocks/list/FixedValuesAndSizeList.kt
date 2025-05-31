package com.unewexp.superblockly.blocks.list

import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.debug.BlockIllegalStateException
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.ConnectorType
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
            ?: throw BlockIllegalStateException(this, "Вы не указали значение для создания списка")

        val countRepeat = repeatTimes.connectedTo?.evaluate() as? Int
            ?: throw BlockIllegalStateException(this, "Количество элементов в списке должно быть типа Int")

        if (countRepeat < 0){
            throw BlockIllegalStateException(this, "Количество элементов не может быть отрицательным")
        }

        return MutableList(countRepeat) { valueRepeat }
    }
}