package parser

import com.google.gson.Gson
import java.io.File

data class EntryData(val terminal:String, val noTerminals:ArrayList<String>)

data class TableEntry(val noTerminal:String, val productions:ArrayList<EntryData>)

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