package app.gui.application

import javafx.scene.control.SelectionMode
import lexer.symbolTable.Token
import tornadofx.*
import troubleshoot.Error
import java.util.*

class MainScreen : View("Analizador de 3 fases") {

    val tokenlist = ArrayList<Token>().observable()

    val errorList = listview<Error>{
        selectionModel.selectionMode = SelectionMode.MULTIPLE
    }

    val fileText = textarea {

    }

   override val root = vbox {
       addClass(Styles.wrapper)

       hbox{
            addClass(Styles.container)
            button("Analisis"){

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
                label("Analisis predictivo")
                tableview<String> {

                }
            }
            vbox{
                addClass(Styles.container)
                label("Traza de analisis sintáctico")
                tableview<String>{

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