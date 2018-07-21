package parser

import com.google.gson.Gson
import javafx.collections.ObservableArray
import javafx.collections.ObservableList
import tornadofx.observable
import java.io.File


data class PredictSetEntry (val noTerminal: String, val production: String , val tokens: String)

class AnalysisTable{
    companion object {

        val terminals = listOf<String>(
                "id",
                "+",
                "-",
                "*",
                "function",
                "(",")",
                "{","}",
                "type",
                ",",
                "value",
                "=",
                "++",
                "--"
        )

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

        fun observableTable(): ObservableList<PredictSetEntry>{
            val jsonString = File("analysisTable.json").readText()

            val gson = Gson()
            val elements = gson.fromJson(jsonString, Array<TableEntry>::class.java)

            val list = ArrayList<PredictSetEntry>()


            elements.forEach { tableEntry: TableEntry ->
                tableEntry.productions.forEach { t ->
                    list.add(PredictSetEntry(tableEntry.noTerminal, t.symbols.toString() , t.terminals.toString() ))
                }
            }

            return list.observable()
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