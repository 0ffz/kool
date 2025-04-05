package de.fabmax.kool.demo.helloworld

import de.fabmax.kool.KoolContext
import de.fabmax.kool.demo.DemoScene
import de.fabmax.kool.modules.ui2.setupUiScene
import de.fabmax.kool.pipeline.ClearColorFill
import de.fabmax.kool.scene.Scene

class HelloComposableUI : DemoScene("Composable UI") {
    override fun Scene.setupMainScene(ctx: KoolContext) {
        setupUiScene(ClearColorFill(Scene.Companion.DEFAULT_CLEAR_COLOR))

        //TODO move compose ui demo here
    }

}
