import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.addUiScene
import de.fabmax.kool.modules.compose.ComposableSurface
import de.fabmax.kool.modules.compose.composables.Box
import de.fabmax.kool.modules.compose.composables.Button
import de.fabmax.kool.modules.compose.composables.Text
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.background
import de.fabmax.kool.modules.compose.modifiers.onClick
import de.fabmax.kool.modules.compose.modifiers.size
import de.fabmax.kool.modules.ui2.Grow
import de.fabmax.kool.modules.ui2.RectBackground
import de.fabmax.kool.util.MdColor

//TODO delete, using for faster testing
fun main() {
    KoolApplication(config = KoolConfigJvm()) {
        addUiScene {
            addNode(ComposableSurface(ctx) {
                var buttonText by remember { mutableStateOf(1) }
                Box(Modifier.size(Grow.Std, Grow.Std).background(RectBackground(MdColor.GREY))) {
                    Button(Modifier.onClick { buttonText += 1 }) {
                        Text("Counter: $buttonText")
                    }
                }
            })
        }
    }
}

