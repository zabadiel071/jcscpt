package lexer.recognizers

abstract class AFD(val word:String) {

    var position = -1
    var status = false

    init {
        q0()
    }

    abstract fun q0()

    fun forward(){
        position ++
    }

    fun read() : Char{
        return 'x'
    }
}