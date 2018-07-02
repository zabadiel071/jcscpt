package lexer.recognizers

/**
 * Abstraction for the automatons used as recognizer
 */
abstract class DFA(val word:String) {

    var position = -1
    var status = false

    init {
        q0()
    }

    abstract fun q0()

    abstract fun qError()

    fun forward(){
        position ++
    }

    fun read() : Char{
        return word[position]
    }
}