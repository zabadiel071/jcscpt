package app

import lexer.symbolTable.HashTable
import lexer.symbolTable.Token

fun main(args: Array<String>) {
    val token = Token("int", value = "40")

    val hashtable = HashTable()

    hashtable.push(token)

    hashtable.updateValue("int", "75")

    print(hashtable.getHashTable())

}