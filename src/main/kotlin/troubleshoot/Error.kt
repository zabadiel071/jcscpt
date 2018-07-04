package troubleshoot

/**
 *
 */
class Error(
        val code : Int,
        val row: Int?= 0,
        val column : Int?=0,
        val token : String) {
    override fun toString(): String {
        var s = "On ($row,$column) \t"
        when(code){
            ErrorCodes.INVALID_WORD -> s+= "Cannot use $token as identifier"
            ErrorCodes.UNKNOWN_SYMBOL -> s+= "Unknown symbol on $token"
            ErrorCodes.INVALID_STRING -> s+= "String error $token"
            ErrorCodes.NUMBER_FORMAT_ERROR -> s += "Number format is not valid [$token]"
            ErrorCodes.UNKNOWN_ERROR-> s+= "Unexpected error"
        }
        return s
    }
}