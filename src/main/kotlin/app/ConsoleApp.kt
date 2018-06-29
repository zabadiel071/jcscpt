package app

import lexer.Lexer
import java.io.File

fun main(args: Array<String>) {
    val file = File("code.cscpt")
    val code = file.readText()

    val lexer = Lexer(code)

}