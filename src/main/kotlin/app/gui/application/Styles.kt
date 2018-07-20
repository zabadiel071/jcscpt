package app.gui.application

import javafx.scene.paint.Color
import tornadofx.*

class Styles: Stylesheet() {
    companion object {
        val wrapper by cssclass()
    }
    init {
        wrapper{
            padding = box(10.px)
            fontSize = 14.px
        }

        textArea{
            fontSize = 16.px
        }
    }
}