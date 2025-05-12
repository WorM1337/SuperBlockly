package statementTree

import com.example.myfirstapplicatioin.blocks.Block
import java.lang.IllegalArgumentException
import java.util.UUID

object BlocksInfo {
    var blocksMap: MutableMap<UUID, Block> = mutableMapOf()
}



class Node(var blockID: UUID?, var children: MutableList<Node>? = null) {

    fun getBlock() : Block? { // Будет глобальный словарь, который будет содержать все добавленные
        // блоки
        return BlocksInfo.blocksMap.get(blockID)
    }

    fun setChildren() {
        var list = BlocksInfo.blocksMap.get(blockID)?.getInformationForTree()

        list?.let {
            children = mutableListOf()
            for(block in list) {
                children!!.add(
                    Node(block.id)
                )
            }
        }
        children = null
    }

    init {
        if(!BlocksInfo.blocksMap.containsKey(blockID)) {
            throw NoSuchElementException("Ключа $blockID нет в словаре блоков!")
        }
        setChildren()
    }




}

class StatementTree() {
    var root: Node = Node(null)



    fun initFromCurrentNode(currentNode: Node = root) {

        if(currentNode == root) {
            var children: MutableList<Node> = mutableListOf()

            for(block in BlocksInfo.blocksMap.values.toList()) {
                children.add(Node(block.id))
            }
        }
        currentNode.children?.let {
            for(node in currentNode.children!!) {
                initFromCurrentNode()
            }
        }


    }

}