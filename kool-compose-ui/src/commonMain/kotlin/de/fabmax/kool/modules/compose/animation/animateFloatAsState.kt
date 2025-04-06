package de.fabmax.kool.modules.compose.animation

import androidx.compose.runtime.*
import de.fabmax.kool.modules.ui2.AnimatedState
import de.fabmax.kool.modules.ui2.MutableStateValue
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
        val durationMillis = 300L // animation duration: 300 ms
        launch(Dispatchers.RenderLoop) {
            val startTime = withFrameNanos { it }
            while (true) {
                val currentTime = withFrameNanos { it }
                val elapsedTimeMillis = (currentTime - startTime) / 1_000_000L
                // Determine progress as a fraction [0f, 1f]
                val progress = (elapsedTimeMillis / durationMillis.toFloat()).coerceIn(0f, 1f)
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


@Composable
fun <T> MutableStateValue<T>.collectAsState(): State<T> {
    return produceState(this.value) {
        onChange { old, new -> this.value = new }
    }
}

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun <T : Any> LaunchAnimation(animator: AnimatedState<T>) {
    LaunchedEffect(animator.isActive) {
        if (animator.isActive) launch(Dispatchers.RenderLoop) {
            while (animator.isActive) {
                animator.progress(Time.deltaT)
                yield()
            }
        }
    }
}
