package lexer.recognizers

class Identifier (word:String) :AFD(word){

    override fun q0() {
        forward()
    }
}