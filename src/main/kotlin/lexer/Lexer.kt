package lexer

import lexer.Definitions.Companion.dictionary
import lexer.Definitions.Companion.reservedWords
import lexer.Definitions.Companion.types
import lexer.recognizers.Identifier
import lexer.recognizers.Numeric
import lexer.symbolTable.HashTable
import lexer.symbolTable.Token
import tornadofx.fiveDigits
import tornadofx.observable
import troubleshoot.Error
import troubleshoot.ErrorCodes
import java.util.*

/**
 * Lexical analysis phase
 * It parses a file and get errors, also add this errors to symbol table
 */
class Lexer (val code: String) {
    private val stack = Stack<String>()
    val hashtable = HashTable()

    private var pointer = -1
    private var auxToken = ""

    var lineCounter = 0

    var errorList = ArrayList<Error>().observable()


    /**
     * Main parsing function
     */
    fun read(){
        try {
            auxToken = ""
            pointer ++
            when{
                code[pointer].isLetter() -> wordCheck()         //Check if is an identifier, reserved word, or valid word expression
                code[pointer].isDigit() ->  numericCheck()       //Check if is a numeric value
                else -> when(code[pointer]){
                    '"' -> stringCheck()                        //Check if is an string value
                    ' ','\t' -> read()                     //Just to verify tabs, line breaks and spaces
                    '\n' -> { lineCounter++ ; read() }
                    '/' -> commentCheck()
                    else -> symbolCheck()                       //Check if is a valid symbol
                }
            }
        }catch (e:IndexOutOfBoundsException){}
    }

    /**
     * Reserved words and identifiers
     */
    private fun wordCheck() {
        auxToken += code[pointer]
        pointer ++
        if (pointer < code.length && code[pointer].isLetterOrDigit())
            wordCheck()
        else{
            if (isReserved(auxToken)){
                stack.push(auxToken)
                hashtable.push(Token(auxToken,category = "RW"))
            }else{
                if(isValidIdentifier(auxToken)){
                    if (!stack.empty() && types.contains(stack.peek()))
                        pushID(auxToken,stack.pop())
                    else{
                        stack.clear()
                        pushID(auxToken)
                    }
                    stack.push(auxToken)
                }else{
                    //Generate errors
                    generateError(ErrorCodes.INVALID_WORD, auxToken, lineCounter)
                }
            }
            pointer --
            this.read()
        }
    }

    /**
     *
     */
    private fun numericCheck() {
        auxToken += code[pointer]
        pointer++
        if (pointer < code.length && (code[pointer].isLetterOrDigit() || code[pointer] == '.'))
            numericCheck()
        else{
            if (isNumeric(auxToken)){
                if (!stack.empty() && stack.pop() == "="){
                    updateValue(stack.pop(), auxToken)
                }
            }else
                generateError(ErrorCodes.NUMBER_FORMAT_ERROR, auxToken, lineCounter)
        }
        pointer--
        this.read()
    }

    /**
     *
     */
    private fun stringCheck() {
        pointer++
        if (pointer < code.length && code[pointer] != '"'){
            auxToken += code[pointer]
            stringCheck()
        }else{
            pointer++
            if (isValidString(auxToken)){
                if (!stack.empty() && stack.pop() == "=")
                    updateValue(stack.pop(), auxToken)
            }else{
                //Generate error
                generateError(ErrorCodes.INVALID_STRING,auxToken,lineCounter)
            }
        }
        this.read()
    }

    /**
     *
     */
    private fun symbolCheck() {
        auxToken += code[pointer]
        pointer++
        //Operators like ==, !=
        if (code[pointer]== '='){
            auxToken += code[pointer]
            pointer ++
        }else pointer --
        if (checkCharacters(auxToken)){
            if (auxToken == "=")
                stack.push("=")
            else
                stack.clear()
        }else{
            generateError(ErrorCodes.UNEXPECTED_SYMBOL, auxToken, lineCounter)
        }
        this.read()
    }

    /**
     *
     */
    private fun commentCheck() {
        auxToken += code[pointer]
        pointer++
        if (code[pointer] == '/'){
            skipLine()
        }else{
            pointer --
            symbolCheck()
        }
    }

    private fun skipLine() {
        pointer++
        if(code[pointer] == '\n'){
            lineCounter ++
            read()
        }else{
            skipLine()
        }
    }

    private fun pushID(token: String, type: String = "") {
        if (type != ""){
            val length = getTypeLength(type)
            this.hashtable.push(Token(token,type,length = length, position = lineCounter,category = "id"))
        }else{
            this.hashtable.push(Token(token,type,position = lineCounter,category = "id"))
        }
    }

    private fun getTypeLength(type: String): Int {
        return Definitions.lengths[type]!!
    }

    private fun isValidIdentifier(token: String): Boolean {
        return Identifier(token).status
    }

    private fun isReserved(token: String): Boolean {
        return reservedWords.contains(token)
    }

    private fun isNumeric(token: String): Boolean {
        return Numeric(auxToken).status
    }

    private fun isValidString(token: String): Boolean {
        return auxToken.all { c : Char ->
            c.isLetterOrDigit() || dictionary.contains("$c")
        }
    }

    private fun checkCharacters(chars: String): Boolean {
        return dictionary.contains(chars)
    }

    private fun updateValue(key: String, value: String) {
        hashtable.updateValue(key,value)
    }

    /**
     *
     */
    private fun generateError(code: Int, token: String, line: Int) {
        errorList.add(troubleshoot.Error(code,line,token))
    }

}
