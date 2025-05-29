package com.unewexp.superblockly.blocks.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.myfirstapplicatioin.blocks.Block
import com.example.myfirstapplicatioin.model.Connector
import com.unewexp.superblockly.enums.BlockType
import com.unewexp.superblockly.enums.CompareType
import com.unewexp.superblockly.enums.ConnectorType
import java.util.UUID

//@Serializable
//data class SerializedDataCompareBlock(
//    val blockId: String,
//    val compareType: CompareType,
//    val connectedLeftBlockId: String?,
//    val connectedRightBlockId: String?,
//)

class CompareNumbers(
    id: UUID = UUID.randomUUID()
) : Block(id, BlockType.COMPARE_NUMBERS_BLOCK) {

    val outputConnector = Connector(
        connectionType = ConnectorType.OUTPUT,
        sourceBlock = this,
    )

    val leftInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    val rightInputConnector = Connector(
        connectionType = ConnectorType.INPUT,
        sourceBlock = this,
        allowedDataTypes = setOf(Int::class.java)
    )

    var compareType by mutableStateOf(CompareType.EQUAL)

    override fun evaluate(): Boolean {
        val leftValue = leftInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Левое выражение отсутствует или не Int")
        val rightValue = rightInputConnector.connectedTo?.evaluate() as? Int
            ?: throw IllegalStateException("Правое выражение отсутствует или не Int")

        return compareElements(leftValue, rightValue)
    }

    fun compareElements(leftValue: Int, rightValue: Int): Boolean {
        return when (compareType) {
            CompareType.EQUAL -> leftValue == rightValue
            CompareType.GREATER_EQUAL -> leftValue >= rightValue
            CompareType.LESS_EQUAL -> leftValue <= rightValue
            CompareType.NOT_EQUAL -> leftValue != rightValue
            CompareType.GREATER -> leftValue > rightValue
            CompareType.LESS -> leftValue < rightValue
        }
    }

//    companion object {
//
//        // Сериализация объекта в ByteArray
//        fun CompareNumbers.toByteArray(): ByteArray {
//            val data = SerializedDataCompareBlock(
//                blockId = this.id.toString(),
//                compareType = this.compareType,
//                connectedLeftBlockId = this.leftInputConnector.connectedTo?.id?.toString(),
//                connectedRightBlockId = this.rightInputConnector.connectedTo?.id?.toString()
//            )
//            return Json.encodeToString(data).toByteArray()
//        }
//
//        // Десериализация из ByteArray и восстановление блока и связей
//        fun ByteArray.toCompareNumbers(): Pair<CompareNumbers, List<Pair<Connector, String>>> {
//            val jsonString = String(this)
//            val data = Json.decodeFromString<SerializedDataCompareBlock>(jsonString)
//
//            val block = CompareNumbers(UUID.fromString(data.blockId))
//            block.compareType = data.compareType
//
//            val connections = mutableListOf<Pair<Connector, String>>()
//
//            data.connectedLeftBlockId?.let {
//                connections.add(block.leftInputConnector to it)
//            }
//
//            data.connectedRightBlockId?.let {
//                connections.add(block.rightInputConnector to it)
//            }
//
//            return block to connections
//        }
//    }
}
