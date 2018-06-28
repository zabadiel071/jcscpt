package symbolTable

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


    fun readHash(i: Int): Long{
        //TODO: Implement
        return 0
    }

    /**
     *
     */
    fun readToken(i: Int): Token{
        //TODO: Implement
        return Token(0,"")
    }


    fun readKey(i: Int): String {
        //TODO: Implement
        return ""
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