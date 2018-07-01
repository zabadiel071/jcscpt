package app

import lexer.symbolTable.HashTable
import lexer.symbolTable.Token

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

}