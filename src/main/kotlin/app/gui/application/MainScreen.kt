package app.gui.application

import javafx.collections.ObservableList
import javafx.scene.control.SelectionMode
import javafx.stage.Modality
import javafx.stage.StageStyle
import lexer.Lexer
import lexer.symbolTable.Token
import tornadofx.*
import troubleshoot.Error
import kotlin.collections.ArrayList

class MainScreen : View("Analizador de 3 fases") {

    val tokenlist = ArrayList<Token>().observable()

    val errorList = listview<Error>{
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        this.prefWidth = 500.0
    }

    val fileText = textarea {

    }

    var lexer : Lexer? = null

   override val root = vbox {
       addClass(Styles.wrapper)

       hbox{
            addClass(Styles.container)
            button("Analisis"){
                action {
                    lexer = Lexer(fileText.text)
                    lexer!!.read()
                    tokenlist.clear()
                    errorList.items.clear()
                    runAsync {
                        lexer!!.hashTable.getHashTable()
                    } ui { observableList: ObservableList<Token> ->
                        observableList.forEach { t:Token -> tokenlist.add(t!!) }
                        if (lexer!!.errorList.isNotEmpty()){
                            lexer!!.errorList.forEach { error: Error -> errorList.items.add(error) }
                        }else{
                            //Start syntax analysis
                        }
                    }
                }
            }
           button ("Tabla de analisis predictivo "){
               action {
                   find<Popup>().openModal(stageStyle = StageStyle.UTILITY,
                           modality = Modality.NONE)
               }
           }
       }
       hbox{
            addClass(Styles.container)
            add(fileText)

            tableview(tokenlist){
               column("Token",Token::token)
               column("Type",Token::type)
               column("Length", Token::length)
               column("Row", Token::row)
               column("Column", Token::column)
               column("Value", Token::value)
               column("Scope", Token::scope)
               column("Category",Token::category)
            }
       }

       hbox{
           addClass(Styles.container)

            vbox{
                addClass(Styles.container)
                label("Traza de analisis sintáctico")
                tableview<String>{
                    prefWidth = 600.0
                }
            }

            vbox{
                addClass(Styles.container)
                label("Resultados")
                add(errorList)
            }
       }
   }

}