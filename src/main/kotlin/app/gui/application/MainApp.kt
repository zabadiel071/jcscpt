package application

import tornadofx.App
import tornadofx.importStylesheet

class MainApp : App() {
    override val primaryView = MainScreen::class

    init{
        importStylesheet(Styles::class)
    }
}