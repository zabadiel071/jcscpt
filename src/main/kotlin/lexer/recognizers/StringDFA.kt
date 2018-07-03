package lexer.recognizers

import lexer.Definitions.Companion.dictionary

class StringDFA(word:String) : DFA(word){

    override fun q0() {
        forward()
        try {
            when(read()){
                '"' -> q1()
                else -> qError()
            }
        }catch (e:StringIndexOutOfBoundsException){}
    }

    private fun q1() {
        forward()
        try {
            val read = read()
            when{
                read.isLetterOrDigit() -> q1()
                read() == '\\' -> q3()
                read() == '"' -> q2()
                dictionary.contains("$read")->q1()
                else->qError()
            }
        }catch (e:StringIndexOutOfBoundsException){}
    }

    private fun q2() {
        forward()
        try {
            val read = read()
            when{
                read.isLetterOrDigit() -> q1()
                read() == '"' -> q2()
                dictionary.contains("$read")->q1()
                else->qError()
            }
        }catch (e:StringIndexOutOfBoundsException){status = true}
    }

    private fun q3() {
        try{
            val read = read()
            q2()
        }catch(e:StringIndexOutOfBoundsException){}
    }

    override fun qError() {
        forward()
        try {
            read()
            qError()
        }catch (e:StringIndexOutOfBoundsException){}
    }


}