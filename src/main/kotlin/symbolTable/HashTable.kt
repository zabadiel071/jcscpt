package symbolTable

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
     * @param key: String
     * @return Token
     */
    fun get(key: String): Token?{
        var hash = this.hash(key)
        var token: Token? = null
        for (i in 0..super.registers){
            if (hash == super.readHash(i)){
                if (key.equals(super.readKey(i)))    {
                    token = this.readToken(i)
                    break
                }else{
                    hash += 1000
                }
            }
        }
        return token;
    }
}