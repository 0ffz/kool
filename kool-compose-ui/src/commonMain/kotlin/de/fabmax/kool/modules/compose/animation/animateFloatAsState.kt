package de.fabmax.kool.modules.compose.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import de.fabmax.kool.util.RenderLoop
import de.fabmax.kool.util.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

//TODO figure out more general way, look at how compose android does it, any public libraries we can use for it?
@Composable
fun animateFloatAsState(targetValue: Float): State<Float> {
    // The animated value that will be updated over time
    val animatedValue = remember { mutableStateOf(targetValue) }

    LaunchedEffect(targetValue) {
        val startValue = animatedValue.value
        val durationMillis = 1000L // animation duration: 1 second

        launch(Dispatchers.RenderLoop) {
            var startTime = 0f // initial frame time in nanoseconds
            while (true) {
                // Calculate elapsed time in milliseconds
                startTime += Time.deltaT * 1000
                // Determine progress as a fraction [0f, 1f]
                val progress = (startTime / durationMillis.toFloat()).coerceIn(0f, 1f)
                // Update the animated value based on progress
                animatedValue.value = startValue + (targetValue - startValue) * progress

                // Break out of the loop when the animation is complete
                if (progress >= 1f) break
                yield()
            }
        }
    }

    return animatedValue
}
