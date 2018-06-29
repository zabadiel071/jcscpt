package lexer

import symbolTable.HashTable
import java.io.File
import java.util.*

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
