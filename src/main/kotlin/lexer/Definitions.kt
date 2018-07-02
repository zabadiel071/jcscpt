package lexer

class Definitions{
    companion object {
        val reservedWords = listOf<String>(
                "int","double","float","string","return","function",
                "if","else","return","for","while", "true", "false"
        )
        val types = listOf<String>("int", "double", "boolean" , "string")

        val dictionary = listOf<String>(
                "=", "+", "-", "*","/", "%",
                "==","!=", "(", ")", "{", "}",
                ":", ",", ".","\\"," "
        )

        val lengths = hashMapOf<String,Int>(
                Pair("int",4),
                Pair("double",8),
                Pair("float",4),
                Pair("string",0)
        )
    }
}