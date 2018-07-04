package lexer

import lexer.Definitions.Companion.delimiters
import lexer.Definitions.Companion.dictionary
import lexer.Definitions.Companion.lengths
import lexer.Definitions.Companion.reservedWords
import lexer.Definitions.Companion.types
import lexer.Definitions.Companion.operators
import lexer.Definitions.Companion.scopeFinalToken
import lexer.Definitions.Companion.scopeInitToken
import lexer.recognizers.IdentifierDFA
import lexer.recognizers.FloatDFA
import lexer.recognizers.IntegerDFA
import lexer.recognizers.StringDFA
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

    private val typeStack = Stack<String>()
    val hashTable = HashTable()

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
        if ( pointer < code.length
                && ( code[pointer].isLetterOrDigit() || !isDelimiter(code[pointer]))
                && !isOperator("${code[pointer]}")
                )
            wordCheck()
        else{
            if (isReserved(auxToken)){
                typeStack.push(auxToken)
                hashTable.push(Token(auxToken,category = Categories.RESERVED_WORD))
            }else{
                if(isValidIdentifier(auxToken)){
                    if (!typeStack.empty() && types.contains(typeStack.peek()))
                        pushID(auxToken, typeStack.pop())
                    else{
                        typeStack.clear()
                        pushID(auxToken)
                    }
                    typeStack.push(auxToken)
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
            when{
                isFloat(auxToken) -> {
                    hashTable.push(
                            Token(auxToken, Types.FLOAT, lengths.get(Types.FLOAT)!!,row, column,"",scope,category = Categories.NUMERIC)
                    )
                }
                isInteger(auxToken) -> {
                    hashTable.push(
                            Token(auxToken, Types.INTEGER, lengths.get(Types.INTEGER)!!,row, column,"",scope,category = Categories.NUMERIC)
                    )
                }
                else -> {
                    generateError(ErrorCodes.NUMBER_FORMAT_ERROR, auxToken, row)
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
    private fun stringCheck() {
        pointer++
        if (pointer < code.length && code[pointer] != '"'){
            auxToken += code[pointer]
            when{
                code[pointer] == '\n' -> row++
                code[pointer] == '\\' -> {
                    pointer ++
                    auxToken += code[pointer]
                }
            }
            stringCheck()
        }else{
            when {
                pointer >= code.length -> generateError(ErrorCodes.INVALID_STRING, auxToken, row)
                code[pointer] == '"' -> {
                    auxToken += code[pointer]
                }
            }
            pointer++
            when (isValidString(auxToken)){
                true->{
                    hashTable.push(
                            Token(auxToken, Types.STRING, lengths[Types.STRING]!!,row,column,scope = scope,category = Categories.STRING)
                    )
                }
                else -> generateError(ErrorCodes.INVALID_STRING,auxToken, row)
            }
            column += auxToken.length
            pointer--
            this.read()
        }
    }

    /**
     *
     */
    private fun symbolCheck() {
        auxToken += code[pointer]
        //Operators like ==, !=
        if (pointer < code.length && (auxToken=="=" || auxToken=="!")){
            val x = code[pointer + 1]
            when (x){
                '=' -> {
                    pointer++;
                    auxToken += code[pointer]
                }
            }
        }

        when (checkCharacters(auxToken)){
            true ->when{
                        isOperator(auxToken) -> pushOperator(auxToken)
                        else -> pushSymbol(auxToken)
                    }
            false -> generateError(ErrorCodes.UNKNOWN_SYMBOL, auxToken, row)
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
            this.hashTable.push(Token(token,type,length = length, row = row,column = column,scope = scope,category = Categories.IDENTIFIER))
        }else{
            this.hashTable.push(Token(token,type, row = row,column = column,scope = scope,category = Categories.IDENTIFIER))
        }
    }

    /**
     *
     */
    private fun pushOperator(token: String) {
        if (token != ""){
            this.hashTable.push(Token(token, row = row,category = Categories.OPERATOR))
        }
    }

    /**
     *
     */
    private fun pushSymbol(token: String) {
        if (token != ""){
            this.hashTable.push(Token(token,"", 0,row, column,"",category = Categories.SYMBOL ))
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
        return IdentifierDFA(token).status
    }

    private fun isReserved(token: String): Boolean {
        return reservedWords.contains(token)
    }

    private fun isFloat(token: String): Boolean {
        return FloatDFA(token).status
    }

    private fun isInteger(token: String): Boolean {
        return IntegerDFA(token).status
    }

    private fun isOperator(token: String) :  Boolean {
        return operators.contains(token)
    }

    private fun isValidString(token: String): Boolean {
        return StringDFA(token).status
    }

    private fun isScopeFinal(token: String): Boolean {
        return scopeFinalToken.contains(token)
    }

    private fun isScopeInit(token: String): Boolean {
        return scopeInitToken.contains(token)
    }

    private fun isDelimiter(c: Char): Boolean {
        return delimiters.contains(c)
    }

    private fun checkCharacters(chars: String): Boolean {
        return dictionary.contains(chars)
    }

    private fun updateValue(key: String, value: String) {
        hashTable.updateValue(key,value)
    }

    /**
     *
     */
    private fun generateError(code: Int, token: String, row: Int, col: Int = 0) {
        errorList.add(troubleshoot.Error(code, row,col,token=token))
    }

}
