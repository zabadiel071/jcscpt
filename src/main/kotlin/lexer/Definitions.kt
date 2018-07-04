package lexer

class Definitions{
    companion object {

        val reservedWords = listOf<String>(
                "int","float","string","return","function",
                "if","else","return","for","while", "true", "false", "const"
        )

        val types = listOf<String>("int","float", "boolean" , "string")

        val operators = listOf<String>(
                "=", "+", "-", "*","/", "%","==","!=","!"
        )

        val dictionary = listOf<String>(
                "!","¡","=", "+", "-", "*","/", "%","_",
                "==","!=", "(", ")", "{", "}","[","]",
                ":", ",", ".","\\"," ", "\"", "\n", "\t" , "'", "`" , "#"
        )

        val delimiters= listOf<Char>(
                ' ',',','\n','\t',';', '(', ')', '{', '}','!'
        )

        val scopeInitToken = listOf<String>(
                "{"
        )

        val scopeFinalToken = listOf<String>(
                "}"
        )

        val lengths = hashMapOf<String,Int>(
                Pair("int",4),
                Pair("double",8),
                Pair("float",4),
                Pair("string",0)
        )
    }
}

class Categories{
    companion object {
        const val IDENTIFIER = "ID"
        const val OPERATOR = "OP"
        const val RESERVED_WORD = "RW"
        const val SYMBOL = "SY"
        const val NUMERIC = "NU"
        const val STRING = "ST"
        const val DELIMITER = "DEL"
    }
}

class Types{
    companion object {
        const val INTEGER = "int"
        const val FLOAT = "float"
        const val BOOLEAN = "bool"
        const val STRING = "string"
    }
}