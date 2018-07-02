package lexer.symbolTable

import javafx.collections.ObservableList
import tornadofx.observable
import java.util.*

/**
 *  Represents a HashTable, is implemented on a random access file
 */
class HashTable : TokenFileController(){

    /**
     * Generates a hash code for a given string
     * @param key : String
     * @return Long
     */
    private fun hash(key: String): Long{
        var x = 9973L
        key.forEach { char ->
            x *= char.toInt()
        }
        return x % 997
    }

    /**
     * Gets the token row after the given key, if there isn't, returns null
     * @param tokenValue: String
     * @return Token
     */
    fun get(key: String): Token?{
        val hash = hash(key)
        val token = readToken(hash)
        if (!key.equals(token?.token)){        // There is a row with that hash row, but value didn't match
            //Linear test
            return linearTestRead(hash, key)
        }
        return token
    }

    /**
     * Tries a sequential search to find a token, since when a collision appears, the collided token is inserted on the
     * next row available
     * @param start : Long
     * @param key : String
     * @return Token : inserted row
     */
    private fun linearTestRead(start: Long, key: String): Token? {
        var token : Token? = null
        for (i in start..registers()){
            token = readToken(i)
            if (token?.token.equals(key)){
                break
            }else{
                token = null
            }
        }
        return token
    }

    /**
     *  Insert a single token into symbol hash table
     *  @param token : Token
     *  @return Boolean
     */
    fun push(token : Token) : Boolean{
        val hash = this.hash(token.token)
        val tokenInsert :Token? = readToken(hash)
        if( tokenInsert == null  || tokenInsert.token.equals("")){        //It's available, no collision appeared
            return write(hash, token)
        }else{
            if ( token.token.equals(tokenInsert.token) ){
                return false
            }else{
                return linearTestInsert(hash, token)
            }
        }
    }

    /**
     *  Inserts a token linearly when a collision appears
     *  @param start : Long
     *  @param token : Token
     *  @return Boolean : If is inserted
     */
    private fun linearTestInsert(start: Long, token: Token): Boolean {
        var tokenTest : Token?
        var written = false
        for (i in start..registers() + 1){
            tokenTest = readToken(i)
            if (tokenTest == null || tokenTest.token.equals("")){
                written = write(i, token)
                break
            }
            if (tokenTest?.token.equals(token.token)){   //Token has been inserted before
                break
            }
        }
        return written
    }

    /**
     * Gets the contents of the hash table
     * Reads linearly, searching for token rows
     * @return ArrayList<Token>
     */
    fun getHashTable() : ObservableList<Token>{
        var token :Token?
        val list = ArrayList<Token>()
        for (i in 0..registers()){
            token = readToken(i)
            if (token != null && !token.token.equals("")){
                list.add(token)
            }
        }
        return list.observable()
    }

    /**
     * Changes the value assigned in "value" column, yes, as stupid as it sounds
     * @param key : String
     * @param value : String
     */
    fun updateValue(key: String, value: String) {
        val hash = hash(key)
        val token = readToken(hash)
        if (!key.equals(token?.token)){
            linearTestUpdateValue(hash,key,value)
        }else{
            token?.value = value
            write(hash, token!!)
        }
    }

    /**
     * @param start : Long
     * @param key : String
     * @param value : String
     */
    private fun linearTestUpdateValue(start: Long, key: String, value: String) {
        var tokenTest: Token?
        var written  = false
        for (i in start..registers()){
            tokenTest = readToken(i)
            if (tokenTest?.token.equals(key)){
                tokenTest?.value = value
                write(i,tokenTest!!)
                break
            }
        }
    }
}