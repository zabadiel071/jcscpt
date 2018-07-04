package app

import lexer.recognizers.StringDFA

fun main(args: Array<String>) {
    val s = "\"\\\"\""
    print(StringDFA(s).status)
}