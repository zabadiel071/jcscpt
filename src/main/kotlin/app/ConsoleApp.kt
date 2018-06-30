package app

import lexer.Lexer
import symbolTable.HashTable
import symbolTable.Token
import java.io.File

fun main(args: Array<String>) {

    val hashtable = HashTable()

    val token =  Token("int")

    hashtable.push(token)

    print(hashtable.get("int"))
}