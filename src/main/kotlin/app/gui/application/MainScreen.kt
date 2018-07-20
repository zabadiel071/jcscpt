package app.gui.application

import javafx.collections.ObservableList
import javafx.scene.Parent
import javafx.scene.control.SelectionMode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import lexer.Lexer
import lexer.symbolTable.Token
import tornadofx.*
import troubleshoot.Error
import java.util.*

class MainScreen : View() {
    override val root : VBox by fxml()

    //val fileHandler = FileHandler()
    //var lexer:Lexer? = null
    var lexer: Lexer?= null

    init {
        primaryStage.isMaximized = true

    }

    /**
    val errorList = listview<Error>{
        selectionModel.selectionMode = SelectionMode.MULTIPLE
        this.maxHeight = 150.0
    }

    val tokenlist = ArrayList<Token>().observable()

    val fileText = textarea{
        minHeight = 400.0
        maxWidth = 400.0
    }

    /**
    override val root = borderpane {
        title = "Analizador Léxico"

        addClass(Styles.wrapper)

        top{
            hbox {
                button("Analizar") {
                    action {
                        lexer = Lexer(fileText.text)
                        lexer!!.read()
                        tokenlist.clear()
                        errorList.items.clear()
                        runAsync {
                            lexer!!.hashTable.getHashTable()
                        } ui { observableList: ObservableList<Token> ->
                            observableList.forEach { t: Token? -> tokenlist.add(t!!) }
                            if (lexer!!.errorList.isNotEmpty()){
                                lexer!!.errorList.forEach { error : Error -> errorList.items.add(error) }
                            }
                        }
                    }
                }

            }
        }

        left{
            vbox{
                add(fileText)
            }
        }

        right{
            vbox{
                maxWidth = 650.0

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

                tableview<String> {

                }
            }
        }

        bottom{
            vbox {
                text("Resultados del análisis")
                add(errorList)
            }
        }
    }
**/
     * */
}