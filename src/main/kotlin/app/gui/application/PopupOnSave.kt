package application

import tornadofx.*

class PopupOnSave : Fragment() {
    override val root = hbox {
        addClass("wrapper")
        text {
            text = "Se guardarán los cambios en el archivo"
        }
    }
}