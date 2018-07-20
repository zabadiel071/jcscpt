package app.gui.application

import javafx.scene.paint.Color
import tornadofx.*

class Styles: Stylesheet() {
    companion object {
        val wrapper by cssclass()
        val container by cssclass()
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
    }
}