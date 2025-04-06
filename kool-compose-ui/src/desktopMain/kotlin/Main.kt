import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.addUiScene
import de.fabmax.kool.modules.compose.ComposableSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.rendering.Text
import de.fabmax.kool.modules.compose.composables.toolkit.*
import de.fabmax.kool.modules.compose.modifiers.Modifier
import de.fabmax.kool.modules.compose.modifiers.background
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
                    Column {
                        Button(onClick = { buttonText += 1 }) {
                            Text("Counter: $buttonText")
                        }
                        var checked by remember { mutableStateOf(false) }
                        Switch(checked, onCheckedChange = { checked = !it })
                        Checkbox(checked, onCheckedChange = { checked = !it })
                        RadioButton(checked, onCheckedChange = { checked = !it })
                        var value by remember { mutableStateOf(1f) }
                        Slider(value, onValueChange = { value = it }, range = 0f..10f)
                    }
                }
            })
        }
    }
}

