package symbolTable

/**
 *
 */
class Token(
        var token: String,
        var type: String = "",
        var length: Int = 0,
        var position: Int = 0,
        var value: String = "",
        var context: String = "")
{

    override fun toString(): String {
        return "Token(token='$token', type='$type', length=$length, position=$position, value='$value', context='$context')"
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
        const val REGISTER_LENGTH = ( 2*TOKEN_LENGTH + 2*TYPE_LENGTH + 4 + 4 + 2*VALUE_LENGTH + 2*CONTEXT_LENGTH).toLong()
    }
}