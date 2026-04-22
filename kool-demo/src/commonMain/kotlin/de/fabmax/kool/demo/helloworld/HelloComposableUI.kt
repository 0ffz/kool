package de.fabmax.kool.demo.helloworld

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import de.fabmax.kool.KoolContext
import de.fabmax.kool.demo.DemoScene
import de.fabmax.kool.input.KeyboardInput
import de.fabmax.kool.modules.compose.ExperimentalKoolComposeAPI
import de.fabmax.kool.modules.compose.LocalColors
import de.fabmax.kool.modules.compose.LocalSizes
import de.fabmax.kool.modules.compose.addComposableSurface
import de.fabmax.kool.modules.compose.composables.layout.Box
import de.fabmax.kool.modules.compose.composables.layout.Column
import de.fabmax.kool.modules.compose.composables.layout.Row
import de.fabmax.kool.modules.compose.composables.rendering.Text
import de.fabmax.kool.modules.compose.composables.toolkit.Button
import de.fabmax.kool.modules.compose.modifiers.drawBehind
import de.fabmax.kool.modules.compose.modifiers.onClick
import de.fabmax.kool.modules.ui2.setupUiScene
import de.fabmax.kool.pipeline.ClearColorFill
import de.fabmax.kool.scene.Scene
import de.fabmax.kool.util.Color
import me.dvyy.compose.mini.layout.jetpack.Alignment
import me.dvyy.compose.mini.layout.jetpack.Arrangement
import me.dvyy.compose.mini.layout.modifiers.*
import me.dvyy.compose.mini.modifier.Modifier

class HelloComposableUI : DemoScene("Composable UI") {
    @OptIn(ExperimentalKoolComposeAPI::class)
    override fun Scene.setupMainScene(ctx: KoolContext) {
        var sizeY by mutableStateOf(100.dp)
        KeyboardInput.addKeyListener(KeyboardInput.KEY_CURSOR_DOWN, "") {
            sizeY += 5.dp
        }
        KeyboardInput.addKeyListener(KeyboardInput.KEY_CURSOR_UP, "") {
            sizeY -= 5.dp
        }
        setupUiScene(ClearColorFill(Scene.DEFAULT_CLEAR_COLOR))

        addComposableSurface {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    Modifier.drawColored(LocalColors.current.background).padding(4.dp).sizeIn(maxWidth = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
//                    val tasks = remember { mutableStateListOf("Task 1") }
                    Text(
                        "My task list",
                        Modifier.align(Alignment.CenterHorizontally),
                        font = LocalSizes.current.largeText
                    )
                    val tasks = remember { mutableStateListOf("Task 1") }
                    for (task in tasks) {
                        Text(task, Modifier.onClick { tasks.remove(task) })
                    }
                    Button(onClick = {
                        println("Clicked, now $tasks")
                        tasks += "New task"
                    }) { Text("New task") }
                }
            }
        }
        return
        addComposableSurface {
            Box(Modifier.drawBehind {
                surface.getMeshLayer(0).uiPrimitives.rect(x, y, width, height, clipBounds, Color.BLACK)
            }) {
                val lipsum =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Box(Modifier.width(sizeY).align(Alignment.Top).drawColored(Color.RED)) {
                        Text(lipsum)
                    }
                    Box(Modifier.height(200.dp).weight(1f).drawColored(Color.ORANGE)) {
                        Text("Centered!", Modifier.align(Alignment.Center))
                    }
                    Box(Modifier.height(200.dp).weight(1f).drawColored(Color.RED).padding(10.dp).onClick {
                        println("Clicked third!"); false
                    }) {
                        var count by remember { mutableStateOf(0) }
                        Row {
                            Button(onClick = { count++ }) { Text("Clicked $count") }
                            Text(lipsum, Modifier)
                        }
                    }
                }
            }
//            Column(
//                verticalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                    repeat(count) {
//                        Text("Hello World $it")
//                    Box(Modifier.backgroundColor(Color.RED)) {  }
//                    Button(onClick = { println("Clicked!") }) { Text("Hello World $it") }
//                    }
//            LaunchedEffect(Unit) {
//                while (true) {
//                    sizeY += 1.dp
//                    delayFrames(1)
//                }
//            }
        }
    }
//        return
//        @OptIn(ExperimentalKoolComposeAPI::class)
//        addComposableSurface {
//            FloatingWindow("Sample UI Elements", Vec2f(50f, 50f), layer = 300) {
//                var buttonText by remember { mutableStateOf(1) }
//                Button(onClick = { buttonText += 1 }) {
//                    Text("Counter: $buttonText")
//                }
//                var checked by remember { mutableStateOf(false) }
//                Row {
//                    Switch(checked, onCheckedChange = { checked = it })
//                    Checkbox(checked, onCheckedChange = { checked = it })
//                    RadioButton(checked, onCheckedChange = { checked = it })
//                }
//                var value by remember { mutableStateOf(1f) }
//                Slider(value, onValueChange = { value = it }, range = 0f..10f)
//
//                var expanded by remember { mutableStateOf(false) }
//                val items = listOf("Item 1", "Item 2", "Item 3", "Item with longer title")
//                var selected by remember { mutableStateOf(0) }
//                DropdownButton(onClick = { expanded = !expanded }) {
//                    Text(items[selected])
//                }
//                DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
//                    items.forEachIndexed { index, item ->
//                        key(item) {
//                            DropdownMenuItem(
//                                text = { Text(item) },
//                                onClick = { selected = index; expanded = false }
//                            )
//                        }
//                    }
//                }
//
//                var text by remember { mutableStateOf("Hello World") }
//                TextField(text, onValueChange = { text = it }, modifier = Modifier.fillMaxWidth())
//                AttributedText(
//                    TextLine(
//                        listOf(
//                            "Attributed Text!" to TextAttributes(
//                                font = MsdfFont.DEFAULT_FONT,
//                                color = MdColor.RED
//                            )
//                        )
//                    ), modifier = Modifier.fillMaxWidth()
//                )
//            }
//
//            FloatingWindow("Scroll container", Vec2f(250f, 50f), layer = 100) {
//                ScrollArea(modifier = Modifier.size(200.dp, 300.dp)) {
//                    Column(Modifier.fillMaxSize()) {
//                        repeat(100) {
//                            Text("Hello World $it")
//                        }
//                        Text("Some item with really long text as an example.")
//                    }
//                }
//            }
//        }
}

@Stable
fun Modifier.drawColored(color: Color) = drawBehind {
    surface.getMeshLayer(0).uiPrimitives.rect(x, y, width, height, clipBounds, color)
}