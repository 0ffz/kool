import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.fabmax.kool.KoolApplication
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.addScene
import de.fabmax.kool.addUiScene
import de.fabmax.kool.math.Vec3f
import de.fabmax.kool.math.deg
import de.fabmax.kool.modules.compose.addComposableSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.rendering.Text
import de.fabmax.kool.modules.compose.composables.toolkit.*
import de.fabmax.kool.modules.compose.modifiers.background
import de.fabmax.kool.modules.compose.modifiers.dragListener
import de.fabmax.kool.modules.compose.modifiers.fillMaxWidth
import de.fabmax.kool.modules.compose.modifiers.margin
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.modules.ui2.*
import de.fabmax.kool.scene.addColorMesh
import de.fabmax.kool.scene.defaultOrbitCamera
import de.fabmax.kool.util.Color
import de.fabmax.kool.util.MdColor
import de.fabmax.kool.util.Time
import me.dvyy.compose.mini.modifier.Modifier

//TODO delete, using for faster testing
fun main() {
    KoolApplication(config = KoolConfigJvm()) {
        addScene {
            defaultOrbitCamera()

            addColorMesh {
                generate {
                    cube {
                        colored()
                    }
                }
                shader = KslPbrShader {
                    color { vertexColor() }
                    metallic(0f)
                    roughness(0.25f)
                }
                onUpdate {
                    transform.rotate(45f.deg * Time.deltaT, Vec3f.X_AXIS)
                }
            }

            lighting.singleDirectionalLight {
                setup(Vec3f(-1f, -1f, -1f))
                setColor(Color.WHITE, 5f)
            }
        }
        addUiScene {
            addComposableSurface {
                var x by remember { mutableStateOf(0.dp) }
                var y by remember { mutableStateOf(0.dp) }
                var buttonText by remember { mutableStateOf(1) }
                Box {
                    Column(
                        Modifier.margin(x, 0.dp, y, 0.dp)
                            .background(RectBackground(MdColor.GREY))
                            .dragListener(object : Draggable {
                                override fun onDrag(ev: PointerEvent) {
                                    x += Dp.fromPx(ev.pointer.delta.x)
                                    y += Dp.fromPx(ev.pointer.delta.y)
                                }
                            })
                    ) {
                        Button(onClick = { buttonText += 1 }) {
                            Text("Counter: $buttonText")
                        }
                        var checked by remember { mutableStateOf(false) }
                        Switch(checked, onCheckedChange = { checked = !it })
                        Checkbox(checked, onCheckedChange = { checked = !it })
                        RadioButton(checked, onCheckedChange = { checked = !it })
                        var value by remember { mutableStateOf(1f) }
                        Slider(value, onValueChange = { value = it }, range = 0f..10f)

                        var expanded by remember { mutableStateOf(false) }
                        Button(onClick = { expanded = !expanded }) {
                            Text("Show Menu")
                        }
                        DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(text = { Text("Item 1") })
                            DropdownMenuItem(text = { Text("Item 2") })
                            DropdownMenuItem(text = { Text("Item 3") })
                        }

                        var text by remember { mutableStateOf("Hello World") }
//                        println("[${Time.frameCount}] (Main) Recomposed at text")
//                        TextField(text, onValueChange = {
//                            println("[${Time.frameCount}] (Main) onValueChange: $it")
//                            text = it
//                        })

                        TextField(text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth())
//                        var selected by remember { mutableStateOf(0) }
//                        val items = listOf("Item 1", "Item 2", "Item 3")
//                        Popup(alignmentX = AlignmentX.Center, alignmentY = AlignmentY.Center) {
//                            Button(onClick = { buttonText++ }) {
//                                Text("Hello centered ${buttonText}")
//                            }
//                        }
//                        Popup(relativeToParent = true, offset = Vec2f(0f, 10f)) {
//                            Box(Modifier.background(RoundRectBackground(Color.BLACK.withAlpha(0.5f), 4.dp))) {
//                                Text("Following parent")
//                            }
//                        }
//                        ComboBox(selected, items, onItemSelected = { selected = it })
                    }
                }
            }
        }
    }
}

