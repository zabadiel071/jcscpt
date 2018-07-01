package app

import lexer.Lexer
import symbolTable.HashTable
import symbolTable.Token
import symbolTable.TokenFileController
import java.io.File

fun main(args: Array<String>) {
    val token = Token("int")
    val token2 = Token("zint")
    val token3 = Token("nit")

    val hashtable = HashTable()

    hashtable.push(token)
    hashtable.push(token2)
    hashtable.push(token3)
    hashtable.push(token)

    print(hashtable.getHashTable())

    hashtable.finish();
}