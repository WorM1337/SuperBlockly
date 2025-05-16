package com.unewexp.superblockly.blocks

object ExecutionContext {
    private val variables = mutableMapOf<String, Any?>()
    private val variableTypes = mutableMapOf<String, Class<*>>() // Class<*> означает что туда будет поступать класс неопределенного типа

    fun setVariable(name: String, value: Any?) {
        variables[name] = value
        if (value != null) {
            variableTypes[name] = value.javaClass
        }
    }

    fun getVariable(name: String): Any? = variables[name]

    fun hasVariable(name: String): Boolean = name in variables

    fun getVariableNames(): Set<String> = variables.keys

    fun getVariableType(name: String): Class<*>? = variableTypes[name]
}