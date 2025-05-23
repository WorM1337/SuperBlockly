package com.unewexp.superblockly.blocks

import androidx.compose.runtime.mutableStateListOf
import com.example.myfirstapplicatioin.blocks.Block
import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock
import java.lang.IllegalStateException

object ExecutionContext {
    private val scopes = mutableListOf(mutableMapOf<String, Any?>())
    private val variableTypes = mutableMapOf<String, Class<*>>() // Class<*> означает что туда будет поступать класс неопределенного типа
    private val scopesBlocks = mutableListOf<Block>()

    private val _logs = mutableStateListOf<String>();
    val logs: List<String> get() = _logs; // это типо инкапсуляции, нужно, чтобы мы могли использовать только для чтения



    fun enterNewScope(startBlock: Block) {
        scopes.add(mutableMapOf())
        scopesBlocks.add(startBlock)
    }

    fun exitCurrentScope() {
        if (scopes.size > 1){
            scopes.removeAt(scopes.lastIndex)
        }
        scopesBlocks.removeLastOrNull()
    }

    fun declareVariable(name: String, value: Any?){
        require(!isVariableDeclaredInCurrentScope(name)){
            "Переменная $name уже объявлена в текущей области"
        }

        scopes.last()[name] = value
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
                throw kotlin.IllegalStateException("Несоответствие типов для $name")
            }
        }

        targetScope[name] = value;
    }

    private fun findVariableScope(name: String): MutableMap<String, Any?>? {
        return scopes.asReversed().firstOrNull { it.containsKey(name) }
    }

    private fun isVariableDeclaredInCurrentScope(name: String): Boolean {
        return scopes.last().containsKey(name)
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


    fun getVariableNames(): Set<String> = scopes.flatMap{it.keys}.toSet() // flatMap проходит по каждой области

    fun getVariableType(name: String): Class<*>? = variableTypes[name]

    fun clearVariables(){
        scopes.clear()
    }

    fun appendLog(message: String){
        _logs.add(message)
    }

    fun clearLogs(){
        _logs.clear()
    }


}