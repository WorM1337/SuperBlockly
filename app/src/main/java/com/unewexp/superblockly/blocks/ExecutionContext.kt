    package com.unewexp.superblockly.blocks

    import androidx.compose.runtime.mutableStateListOf
    import com.example.myfirstapplicatioin.blocks.Block
    import com.unewexp.superblockly.blocks.voidBlocks.VoidBlock

    object ExecutionContext {
        private val scopes = mutableListOf(mutableMapOf<String, Any?>())
        private val variableTypes = mutableMapOf<String, Class<*>>() // Class<*> означает что туда будет поступать класс неопределенного типа

        private val _logs = mutableStateListOf<String>();
        val logs: List<String> get() = _logs; // это типо инкапсуляции, нужно, чтобы мы могли использовать только для чтения


        private val scopesBlocks = mutableListOf<Block>()


        fun enterNewScope(startBlock: Block) {
            pushScope() // для переменных
            scopesBlocks.add(startBlock)
        }

        fun exitCurrentScope() {
            popScope() // для переменных
            scopesBlocks.removeLastOrNull()
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



        fun pushScope(){
            scopes.add(mutableMapOf())
        }

        fun popScope(){
            if (scopes.size > 1){
                scopes.removeAt(scopes.lastIndex)
            }
        }

        fun setVariable(name: String, value: Any?){
            scopes.last()[name] = value
            if (value != null){
                variableTypes[name] = value.javaClass
            }
        }

        fun getVariable(name: String): Any?{
            for (scope in scopes.asReversed()){
                if (name in scope) return scope[name]
            }
            return null
        }

        fun hasVariable(name: String): Boolean{
            return scopes.any{ it.containsKey(name)}
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