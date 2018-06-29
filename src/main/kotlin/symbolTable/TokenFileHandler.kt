package symbolTable

import java.io.EOFException
import java.io.RandomAccessFile

/**
 *
 */
open class TokenFileHandler(val fileName: String) {


    val file = RandomAccessFile(fileName,"rw")
    val registerLength: Long = Token.REGISTER_LENGTH

    var registers: Int = 0


    init {
        file.setLength(0)
        file.seek(0)
    }

    fun finish(){
        file.setLength(0)
        file.close()
    }

    /**
     * Insert Token row
     */
    fun write(token: Token) {
        file.seek(registers * registerLength)
        file.writeLong(token.hashValue)
        writeString(token.token, Token.TOKEN_LENGTH)
        writeString(token.type, Token.TYPE_LENGTH)
        file.writeInt(token.length)
        file.writeInt(token.position)
        writeString(token.value, Token.VALUE_LENGTH)
        writeString(token.context, Token.CONTEXT_LENGTH)
        file.seek(0)
        registers ++
    }


    /**
     * @param i : Int Index of the row
     * @return Token
     */
    fun readHash(i: Int): Long{
        try{
            file.seek(i*registerLength)
            val hash = file.readLong()
            file.seek(0)
            return hash
        }catch(e:EOFException){
            return 0
        }
    }

    /**
     * @param i : Int Row index
     * @return Token
     */
    fun readToken(i: Int): Token?{
        try{
            file.seek(i * registerLength)

            val hashValue = file.readLong()
            val token  = readString(Token.TOKEN_LENGTH)
            val type = readString(Token.TYPE_LENGTH)
            val length = file.readInt()
            val position = file.readInt()
            val value = readString(Token.VALUE_LENGTH)
            val context = readString(Token.CONTEXT_LENGTH)

            file.seek(0)

            return Token(hashValue, token, type, length, position, value, context)
        }catch(e: EOFException){
            e.printStackTrace()
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