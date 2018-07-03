package lexer.recognizers

/**
 *
 */
class FloatDFA(word:String) : DFA(word){
    override fun q0() {
        forward()
        try {
            when(read().isDigit()){
                true -> q1()
                false -> qError()
            }
        }catch (e: StringIndexOutOfBoundsException){}
    }

    /**
     *
     */
    private fun q1() {
        forward()
        try {
            when(read().isDigit()){
                true -> q1()
                false -> when(read()){
                    '.' -> q2()
                    else -> qError()
                }
            }
        }catch (e:StringIndexOutOfBoundsException){}
    }

    /**
     *
     */
    private fun q2() {
        forward()
        try{
            when(read().isDigit()){
                true -> q3()
                false -> qError()
            }
        }catch (e: StringIndexOutOfBoundsException){this.status = true}
    }

    /**
     *
     */
    private fun q3() {
        forward()
        try {
            when(read().isDigit()){
                true -> q3()
                false -> qError()
            }
        }catch (e:StringIndexOutOfBoundsException){status = true}
    }

    /**
     *
     */
    override fun qError() {
        forward()
        try {
            read()
            qError()
        }catch (e:StringIndexOutOfBoundsException){}
    }

}