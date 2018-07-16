package parser

data class EntryData(val terminal:String,
                     val symbols:ArrayList<String>
){
    override fun toString(): String {
        return "(terminal=$terminal, symbols=$symbols)"
    }
}

data class TableEntry(val noTerminal:String, val productions:ArrayList<EntryData>, val sync:ArrayList<String>)
