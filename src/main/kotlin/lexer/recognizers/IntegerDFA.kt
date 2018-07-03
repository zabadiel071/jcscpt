package lexer.recognizers

class IntegerDFA(word:String) : DFA(word) {
    override fun q0() {
        forward()
        try {
            when(read().isDigit()){
                true -> q1()
                false -> qError()
            }
        }catch (e:IndexOutOfBoundsException){}
    }

    private fun q1() {
        forward()
        try {
            when(read().isDigit()){
                true -> q1()
                false -> qError()
            }
        }catch (e:IndexOutOfBoundsException){status = true}
    }

    override fun qError() {
        forward()
        try {
            read()
            qError()
        }catch (e:StringIndexOutOfBoundsException){}
    }
}