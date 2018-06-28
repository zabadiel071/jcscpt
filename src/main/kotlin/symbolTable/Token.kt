package symbolTable

/**
 *
 */
class Token(
        val hashValue: Long = 0,
        val token: String,
        val type: String = "",
        val length: Int = 0,
        val position: Int = 0,
        val value: String = "",
        val context: String = "")
{

    override fun toString(): String {
        return "Token(hashValue='$hashValue', token='$token', type='$type', length=$length, position=$position, value=$value, context='$context')"
    }



    /**
     * Values length to assign into file
     */
    companion object{
        const val TOKEN_LENGTH = 64
        const val TYPE_LENGTH = 32
        const val VALUE_LENGTH = 255
        const val CONTEXT_LENGTH = 255

        /**
         * Size that a token will occupy
         */
        const val REGISTER_LENGTH = (8 + TOKEN_LENGTH + TYPE_LENGTH + 4 + 4 + VALUE_LENGTH + CONTEXT_LENGTH).toLong()
    }
}