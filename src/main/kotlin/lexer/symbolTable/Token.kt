package lexer.symbolTable

/**
 *
 */
class Token(
        var token: String,
        var type: String = "",
        var length: Int = 0,
        var row: Int = 0,
        val column: Int = 0,
        var value: String = "",
        var scope: Int = 0,
        var category: String = "")
{

    override fun toString(): String {
        return "Token(token='$token', type='$type', length=$length, row=$row, column=$column,value='$value', scope='$scope', category='$category')"
    }

    /**
     * Values length to assign into file
     */
    companion object{
        const val TOKEN_LENGTH = 64
        const val TYPE_LENGTH = 32
        const val VALUE_LENGTH = 255
        const val CATEGORY_LENGTH = 255
        /**
         * Size that a token will occupy
         */
        const val REGISTER_LENGTH = (
                    2*TOKEN_LENGTH +
                    2*TYPE_LENGTH +
                    4 +                 // Length
                    4 +                 // Row
                    4 +                 // Column
                    2*VALUE_LENGTH +
                    4 +                 // Scope
                    2*CATEGORY_LENGTH
                ).toLong()
    }
}