package symbolTable

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
        for (i in 0..super.registers){
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
     *
     */
    fun push(token : Token) : Boolean{
        return true
    }
}

fun main(args: Array<String>) {
    var hashtable = HashTable()
    hashtable.hash("int")
}