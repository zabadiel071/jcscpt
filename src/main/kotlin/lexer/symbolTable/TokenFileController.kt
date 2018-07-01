package lexer.symbolTable

import java.io.EOFException
import java.io.RandomAccessFile

/**
 *  Low level class to operate the random access file which storage the hashtable
 */
open class TokenFileController( ) {

    val file = RandomAccessFile("ts.dat","rw")              //
    val registerLength: Long = Token.REGISTER_LENGTH        // Length of each row
    fun registers (): Long = file.length() / registerLength    // All registers, filled or not


    init {
        file.setLength(0)
        file.seek(0)
    }

    /**
     * Insert Token row
     * @param i : Long Index of the row to be inserted
     * @param token : Token Token to be inserted
     * @return Boolean if it was correctly written
     */
    fun write(i:Long,token: Token) : Boolean {
        file.seek(i * registerLength)
        return try {
            //file.writeLong(token.hashValue)
            writeString(token.token, Token.TOKEN_LENGTH)
            writeString(token.type, Token.TYPE_LENGTH)
            file.writeInt(token.length)
            file.writeInt(token.position)
            writeString(token.value, Token.VALUE_LENGTH)
            writeString(token.context, Token.CONTEXT_LENGTH)
            writeString(token.category, Token.CATEGORY_LENGTH)
            file.seek(0)
            true
        }catch (e:EOFException){
            e.printStackTrace()
            false
        }
    }

    /**
     * Gets token on desired index
     * @param i : Int Row index
     * @return Token
     */
    fun readToken(i: Long): Token?{
        try{
            file.seek(i * registerLength)
            val token  = readString(Token.TOKEN_LENGTH)
            val type = readString(Token.TYPE_LENGTH)
            val length = file.readInt()
            val position = file.readInt()
            val value = readString(Token.VALUE_LENGTH)
            val context = readString(Token.CONTEXT_LENGTH)
            val category = readString(Token.CATEGORY_LENGTH)
            file.seek(0)

            return Token(token, type, length, position, value, context, category)
        }catch(e: EOFException){
            //e.printStackTrace()
            return null
        }
    }

    /**
     * Writes a string with the desired length into file at current position
     * @param s : String
     * @param length : Int
     */
    private fun writeString(s: String, length: Int) {
        var stringBuffer = StringBuffer()
        if (s != null)
            stringBuffer.append(s)
        stringBuffer.setLength(length)
        file.writeChars(stringBuffer.toString())
    }

    /**
     * Read string with the given size
     * @param size : Int
     * @return String
     */
    private fun readString(size: Int) : String{
        var s = ""
        for (x in 0..size - 1){
            var char = file.readChar()
            if (char.isLetterOrDigit()){
                s+=char
            }
        }
        return s
    }

}