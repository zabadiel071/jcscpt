package parser

data class EntryData(val terminals:ArrayList<String>,
                     val symbols:ArrayList<String>
){
    override fun toString(): String {
        return "(terminals=$terminals, symbols=$symbols)"
    }
}

data class TableEntry(val noTerminal:String, val productions:ArrayList<EntryData>, val sync:ArrayList<String>)
