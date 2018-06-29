package lexer

class Definitions{
    companion object {
        val reservedWords = listOf<String>(
                "int","double","string","return","function",
                "if","else","return","for","while"
        )
        val types = listOf<String>("int", "double")

        val dictionary = listOf<String>(
                "=", "+", "-", "*","/", "%",
                "==","!=", "(", ")", "{", "}",
                ":", ",", ".","\\"," "
        )
    }
}