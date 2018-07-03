package lexer

import lexer.Definitions.Companion.dictionary
import lexer.Definitions.Companion.reservedWords
import lexer.Definitions.Companion.types
import lexer.Definitions.Companion.operators
import lexer.Definitions.Companion.scopeFinalToken
import lexer.Definitions.Companion.scopeInitToken
import lexer.recognizers.Identifier
import lexer.recognizers.Numeric
import lexer.recognizers.Strings
import lexer.symbolTable.HashTable
import lexer.symbolTable.Token
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

    var row = 0
    var column = 0
    var scope = 0

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
                    '"' ->  {
                        auxToken += code[pointer]
                        stringCheck()
                    }                        //Check if is an string value
                    ' ','\t' -> {
                        column++; read()
                    }                     //Just to verify tabs, row breaks and spaces
                    '\n' -> {
                        row++
                        column = 0
                        read()
                    }
                    '/' -> {
                        commentCheck()
                    }
                    else ->{
                        symbolCheck()                       //Check if is a valid symbol
                    }
                }
            }
        }catch (e:IndexOutOfBoundsException){
        }
    }

    /**
     * Reserved words and identifiers
     * Word is saved with column which started
     */
    private fun wordCheck() {
        auxToken += code[pointer]
        pointer ++
        if (pointer < code.length && code[pointer].isLetterOrDigit())
            wordCheck()
        else{
            if (isReserved(auxToken)){
                stack.push(auxToken)
                hashtable.push(Token(auxToken,category = Categories.RESERVED_WORD))
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
                    generateError(ErrorCodes.INVALID_WORD, auxToken, row)
                }
            }
            column += auxToken.length
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
                generateError(ErrorCodes.NUMBER_FORMAT_ERROR, auxToken, row)
        }
        column += auxToken.length
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
            if (code[pointer] == '\n')
                row++
            stringCheck()
        }else{
            if (pointer >= code.length){
                generateError(ErrorCodes.INVALID_STRING,auxToken, row)
            }else{
                if (code[pointer] == '"')
                    auxToken += code[pointer]
                pointer++
                if (isValidString(auxToken)){
                    if (!stack.empty() && stack.pop() == "=")
                        updateValue(stack.pop(), auxToken)
                }else{
                    //Generate error
                    generateError(ErrorCodes.INVALID_STRING,auxToken, row)
                }
            }
        }
        column += auxToken.length
        pointer--
        this.read()
    }

    /**
     *
     */
    private fun symbolCheck() {
        auxToken += code[pointer]
        pointer++
        //Operators like ==, !=
        if (pointer < code.length){
            if (code[pointer]== '='){
                auxToken += code[pointer]
                pointer ++
            }else pointer --
        }

        if (checkCharacters(auxToken)){
            if (auxToken == "=")
                stack.push("=")
            else{
                if (isOperator(auxToken))
                    pushOperator(auxToken)
                else
                    pushSymbol(auxToken)
                stack.clear()
            }
        }else{
            generateError(ErrorCodes.UNKNOWN_SYMBOL, auxToken, row)
        }
        column += auxToken.length
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
            auxToken = ""
            pointer --
            symbolCheck()
        }
    }

    /**
     *
     */
    private fun skipLine() {
        pointer++
        if(code[pointer] == '\n'){
            row++
            read()
        }else{
            skipLine()
        }
    }

    /**
     *
     */
    private fun pushID(token: String, type: String = "") {
        if (type != ""){
            val length = getTypeLength(type)
            this.hashtable.push(Token(token,type,length = length, row = row,column = column,scope = scope,category = Categories.IDENTIFIER))
        }else{
            this.hashtable.push(Token(token,type, row = row,column = column,scope = scope,category = Categories.IDENTIFIER))
        }
    }

    /**
     *
     */
    private fun pushOperator(token: String) {
        if (token != ""){
            this.hashtable.push(Token(token, row = row,category = Categories.OPERATOR))
        }
    }

    /**
     *
     */
    private fun pushSymbol(token: String) {
        if (token != ""){
            this.hashtable.push(Token(token,"", 0,row, column,"",category = Categories.SYMBOL ))
        }
        if (isScopeInit(token)){
            scope++
        }
        if (isScopeFinal(token)){
            scope--
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

    private fun isOperator(token: String) :  Boolean {
        return operators.contains(token)
    }

    private fun isValidString(token: String): Boolean {
        return Strings(token).status
    }

    private fun isScopeFinal(token: String): Boolean {
        return scopeFinalToken.contains(token)
    }

    private fun isScopeInit(token: String): Boolean {
        return scopeInitToken.contains(token)
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
    private fun generateError(code: Int, token: String, row: Int, col: Int = 0) {
        errorList.add(troubleshoot.Error(code, row,col,token=token))
    }

}
