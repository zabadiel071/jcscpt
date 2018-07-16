package parser

import com.google.gson.Gson
import java.io.File

data class EntryData(val terminal:String,
                     val symbols:ArrayList<String>
){
    override fun toString(): String {
        return "(terminal=$terminal, symbols=$symbols)"
    }
}

data class TableEntry(val noTerminal:String, val productions:ArrayList<EntryData>, val sync:ArrayList<String>)



class AnalysisTable{
    companion object {
        val terminals = listOf<String>("id","+","-","*","/","(",")")

        fun analysisTable(): HashMap<String, ArrayList<EntryData>> {
            val jsonString = File("analysisTable.json").readText()

            val gson = Gson()
            val elements = gson.fromJson(jsonString, Array<TableEntry>::class.java)

            val map = HashMap<String,ArrayList<EntryData>>()

            elements.forEach { tableEntry ->
                map.put(tableEntry.noTerminal,tableEntry.productions)
            }

            return map
        }

        fun syncList(): HashMap<String,ArrayList<String>>{
            val jsonString = File("analysisTable.json").readText()

            val gson = Gson()
            val elements = gson.fromJson(jsonString, Array<TableEntry>::class.java)

            val map = HashMap<String,ArrayList<String>>()

            elements.forEach { tableEntry ->
                map.put(tableEntry.noTerminal,tableEntry.sync)
            }

            return map
        }
    }
}