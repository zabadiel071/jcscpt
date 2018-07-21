package app.gui.application

import javafx.scene.paint.Color
import javafx.scene.text.Font
import sun.font.FontFamily
import tornadofx.*

class Styles: Stylesheet() {
    companion object {
        val wrapper by cssclass()
        val container by cssclass()
        val table by cssclass()
    }
    init {
        wrapper{
            padding = box(10.px)
            fontSize = 14.px
            spacing = 5.px
        }

        container{
            spacing = 8.px
        }

        table{
            padding = box(10.px)
            fontSize = 12.px
            fontFamily = "Comic Sans MS"
        }
    }
}