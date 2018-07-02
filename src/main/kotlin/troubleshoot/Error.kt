package troubleshoot

/**
 *
 */
class Error(
        val code : Int,
        val line : Int?= 0,
        val token : String) {
    override fun toString(): String {
        var s = "On line $line \t"
        when(code){
            ErrorCodes.INVALID_WORD -> s+= "Cannot use $token as recognizer"
            ErrorCodes.UNEXPECTED_SYMBOL -> s+= "Unexpected symbol on $token"
            ErrorCodes.INVALID_STRING -> s+= "Cannot parse string $token"
            ErrorCodes.NUMBER_FORMAT_ERROR -> s += "Number format is not valid [$token]"
            ErrorCodes.UNKNOWN_ERROR-> s+= "Unexpected error"
        }
        return s
    }
}