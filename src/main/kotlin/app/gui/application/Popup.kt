package app.gui.application

import parser.AnalysisTable
import parser.PredictSetEntry
import tornadofx.*

class Popup : Fragment() {

    val predictset = AnalysisTable.observableTable()

    override val root = vbox{
        addClass(Styles.table)
        label("Analisis predictivo")
        tableview(predictset) {
            minWidth = 600.0
            column("No terminal", PredictSetEntry::noTerminal)
            column("Produces", PredictSetEntry::production)
            column("For tokens", PredictSetEntry::tokens)

            columnResizePolicy = SmartResize.POLICY
        }
    }
}