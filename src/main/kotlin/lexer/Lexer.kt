package lexer

import lexer.symbolTable.HashTable
import java.util.*

/**
 * Lexical analysis phase
 * It parses a file and get errors, also add this errors to symbol table
 */
class Lexer (val code: String?) {
    private val stack = Stack<String>()
    val hashtable = HashTable()
    val compilableCode = this.format()

    private var pointer = -1
    private var auxToken = ""


    private fun format(): String {
        var compilableCode: String = ""

        return compilableCode
    }

}
