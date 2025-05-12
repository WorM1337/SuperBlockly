package com.example.myfirstapplicatioin.blocks

import androidx.compose.runtime.mutableStateListOf

import com.example.myfirstapplicatioin.model.Connector
import com.example.myfirstapplicatioin.model.Value

class ExecutionContext {

    private val variables = mutableMapOf<String, Value>()


    fun declareVariable(name: String, value: Value = Value.Undefined){
        variables[name] = value
    }

    fun getVariable(name: String) : Value{
        return variables[name] ?: throw IllegalStateException("Переменная $name не найдена")
    }

    fun getType(name: String): String {
        return when (getVariable(name)) {
            is Value.IntValue -> "Int"
            is Value.StringValue -> "String"
            is Value.BooleanValue -> "Boolean"
            Value.Undefined -> "Undefined"
        }
    }

}