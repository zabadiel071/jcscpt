package symbolTable

import java.util.*

/**
 *
 */
class HashTable : TokenFileController("hashtable"){

    /**
     * Generates a hash code for a given string
     * @param key : String
     * @return Long
     */
    fun hash(key: String): Long{
        var x = 9973L
        key.forEach { char ->
            x *= char.toInt()
        }
        return x % 997
    }

    /**
     * Gets the token row after the given key
     * @param tokenValue: String
     * @return Token
     */
    fun get(tokenValue: String): Token?{
        var hash = this.hash(tokenValue)
        var token: Token? = null
        for (i in 0..registers - 1){
            if (hash == super.readHash(i)){
                val tokenTest = super.readToken(i);
                if(tokenValue.equals(tokenTest?.token)){
                    token = tokenTest
                }else{
                    hash += 1000
                }
            }
        }
        return token;
    }

    /**
      *  Insert a single token into symbol hash table
     */
    fun push(token : Token) : Boolean{
        val hash = this.hash(token.token)
        return write(hash,token)
    }

    /**
     * Gets the contents of the hash table
     * @return ArrayList<Token>
     */
    fun getHashTable() : ArrayList<Token>{
        var hashTable = ArrayList<Token>()
        for (i in 0..registers - 1){
            hashTable.add(readToken(i)!!)
        }
        return hashTable
    }
}