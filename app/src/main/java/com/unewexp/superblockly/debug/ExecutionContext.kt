package com.unewexp.superblockly.debug

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import kotlinx.coroutines.Job

enum class RunProgram{
    RUN,
    DEBUG,
    NONE
}

object ExecutionContext {
    private val _scopes = mutableStateListOf<SnapshotStateMap<String, Any?>>(
        mutableStateMapOf()
    )
    private val variableTypes = mutableMapOf<String, Class<*>>()
    private val scopesBlocks = mutableListOf<Block>()

    internal var executionJob: Job? = null

    var programProgress by mutableStateOf(RunProgram.NONE)

    val scopes: SnapshotStateList<SnapshotStateMap<String, Any?>> get() = _scopes


    fun enterNewScope(startBlock: Block) {
        _scopes.add(mutableStateMapOf())
        scopesBlocks.add(startBlock)
    }

    fun exitCurrentScope() {
        if (scopes.size > 1){
            _scopes.removeAt(scopes.lastIndex)
        }
        scopesBlocks.removeLastOrNull()
    }

    fun declareVariable(name: String, value: Any?){
        require(!isVariableDeclaredInCurrentScope(name)){
            "Переменная $name уже объявлена в текущей области"
        }

        _scopes.last()[name] = value
        if (value != null){
            variableTypes[name] = value.javaClass
        }
    }

    fun changeVariableValue(name: String, value: Any?){
        val targetScope = findVariableScope(name)
            ?: throw IllegalStateException("Переменная $name не существует")

        value?.let{
            val declaredType = variableTypes[name]
            if (declaredType != null && declaredType != it.javaClass){
                throw IllegalStateException("Несоответствие типов для $name")
            }
        }

        targetScope[name] = value;
    }

    private fun findVariableScope(name: String): MutableMap<String, Any?>? {
        return _scopes.asReversed().firstOrNull { it.containsKey(name) }
    }

    private fun isVariableDeclaredInCurrentScope(name: String): Boolean {
        return _scopes.last().containsKey(name)
    }


    fun checkNextBlockInScope() : Block? {
        val current = scopesBlocks.lastOrNull() ?: return null
        return (current as? VoidBlock)?.getNextBlock()
    }

    fun getNextBlockInScope(): Block? {
        val current = scopesBlocks.lastOrNull() ?: return null
        val next = (current as? VoidBlock)?.getNextBlock()

        return if (next != null){
            scopesBlocks[scopesBlocks.lastIndex] = next
            next
        } else {
            null
        }
    }



    fun getVariable(name: String): Any?{
        return findVariableScope(name)?.get(name)
    }

    fun hasVariable(name: String): Boolean{
        return findVariableScope(name) != null
    }


    fun getVariableNames(): Set<String> = scopes.flatMap { it.keys }.toSet()

    fun getVariableType(name: String): Class<*>? = variableTypes[name]

    fun clearVariables(){
        _scopes.clear()
    }



}