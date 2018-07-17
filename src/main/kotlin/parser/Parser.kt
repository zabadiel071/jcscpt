package parser

import java.util.*

class Parser(val tokenList:Queue<String>) {

    val analysisTable = AnalysisTable.analysisTable()
    val syncList = AnalysisTable.syncList()
    val stack = Stack<String>()
    val trace:Queue<String> = LinkedList<String>()

    /**
     *  Following the algorithm in the book
     */
    fun syntaxAnalysis(){
        var token = tokenList.poll()
        stack.push("$")
        stack.push("SCRIPT")
        loop@ while (!stack.empty()){
            val productions = analysisTable[stack.peek()]       //All the productions for the No Terminal given
            when{
                stack.peek() == token -> {
                    val pop = stack.pop()
                    token = tokenList.poll()
                    event(pop)
                }
                isTerminal(stack.peek()) -> {
                    parseError(token)
                    break@loop
                }
                isErrorEntry(token, productions) -> {
                    parseError(token)
                    if (canSync(token,stack.peek()))
                        token = tokenList.poll()
                    else
                        break@loop
                }
                else -> {
                    var symbolsTail:ArrayList<String> = ArrayList()
                    productions!!.forEach { entryData: EntryData ->
                        if (entryData.terminal == token)
                            symbolsTail = entryData.symbols
                    }
                    event(stack.pop(),symbolsTail)
                    (symbolsTail.size-1 downTo 0)
                            .filter { symbolsTail[it] != "" }
                            .forEach { stack.push(symbolsTail[it]) }
                }
            }
        }
    }

    /**
     * Checks if is possible to sync an error
     */
    private fun canSync(token: String?, symbol:String): Boolean {
        val syncItems = syncList[symbol]
        return if (syncItems!=null)
            syncItems!!.contains(token)
        else
            false
    }

    /**
     *
     */
    private fun parseError(s:String){
        trace.add("Unexpected token [$s]")
    }

    /**
     *
     */
    private fun event(s:String){
        trace.add("Match [$s]")
    }

    /**
     *
     */
    private fun event(s: String?, symbols: ArrayList<String>) {
        var production =""
        symbols.forEach { s: String ->
            production += " $s"
        }
        if (production.trim()!="")
            trace.add("$s -> $production")
        else
            trace.add("$s -> E")
    }

    /**
     *
     */
    private fun isErrorEntry(s: String?,productions: ArrayList<EntryData>?): Boolean {
        return productions!!.none { entryData: EntryData ->
            s == entryData.terminal
        }
    }

    /**
     *
     */
    private fun isTerminal(s: String): Boolean {
        return AnalysisTable.terminals.contains(s)
    }


}

fun main(args: Array<String>) {
    val list:Queue<String> = LinkedList<String>()

    list.addAll(listOf("$"))

    //Test with function declarations
    //list.addAll(listOf("function", "id", "(", "type", "id" , ",", "type", "id",")", "{", "}" ))
    //list.addAll(listOf("function", "id", "(", "type", "id" , ",", "type", "id",")", "{", "}","$" ))

    val parser = Parser(list)
    parser.syntaxAnalysis()

    println(parser.trace)
}