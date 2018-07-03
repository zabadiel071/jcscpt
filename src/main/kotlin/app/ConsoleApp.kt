package app

import lexer.recognizers.Strings
import lexer.symbolTable.HashTable
import lexer.symbolTable.Token

fun main(args: Array<String>) {
    val s = "\"\\\"\""
    print(Strings(s).status)
}