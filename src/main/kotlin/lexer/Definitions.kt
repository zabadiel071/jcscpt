package lexer

class Definitions{
    companion object {
        val reservedWords = listOf<String>(
                "int","double","string","return","function",
                "if","else","return","for","while", "true", "false"
        )
        val types = listOf<String>("int", "double", "boolean")

        val dictionary = listOf<String>(
                "=", "+", "-", "*","/", "%",
                "==","!=", "(", ")", "{", "}",
                ":", ",", ".","\\"," "
        )
    }
}