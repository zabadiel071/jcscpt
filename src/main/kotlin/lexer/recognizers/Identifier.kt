package lexer.recognizers

/**
 * Recognizer for identifiers
 */
class Identifier (word:String) : DFA(word){
    override fun q0() {
        forward()
        try {
            when(read().isLetter()){
                true -> q1()
                false -> qError()
            }
        }catch (e:StringIndexOutOfBoundsException){ status = true }
    }

    private fun q1() {
        forward()
        try {
            when(read().isLetterOrDigit()){
                true -> q1()
                false -> {
                    when(read()){
                        '_','$' -> q1()
                        else -> qError()
                    }
                }
            }
        }catch (e:StringIndexOutOfBoundsException){ status = true }
    }

    override fun qError() {
        forward()
        try {
            read()
            qError()
        }catch (e:StringIndexOutOfBoundsException){}
    }
}
