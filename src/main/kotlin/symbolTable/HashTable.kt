package symbolTable

import java.util.*

/**
 *
 */
class HashTable : TokenFileHandler("hashtable"){

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
        val validHash = getValidHash(hash, token.token)
        if (validHash != 0L){
            token.hashValue = validHash
            write(token)
            return true
        }else
            return false
    }

    /**
     * Gets a collision-free hash to insert a new Token
     * @param s : String string to be hashed
     */
    private fun getValidHash(hash : Long, token : String) : Long{
        var validHash = hash
        var tokenTest: Token?
        for (i in 0..registers){
            if (validHash == super.readHash(i)){    //Collision
                if (validHash.equals(super.readToken(i)?.token)){
                    //Collision with same token (Omit)
                    validHash = 0L
                    break
                }
                else
                    validHash += 1000
            }
        }
        return validHash
    }

    fun getHashTable() : ArrayList<Token>{
        var hashTable = ArrayList<Token>()
        for (i in 0..registers - 1){
            hashTable.add(readToken(i)!!)
        }
        return hashTable
    }
}