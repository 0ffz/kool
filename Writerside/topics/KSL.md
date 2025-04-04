<card-summary>The shader language used by kool.</card-summary>

# KSL - kool shading language

> KSL is quite powerful and offers pretty much the same feature set as GLSL, although the syntax is sometimes a
> bit more complicated. However, **documentation is still very much incomplete**. In case you want to dive deep you
> should take a look at the source code of the builtin shaders, to get an idea about how things work.

{ style="warning" }


### Why KSL

Maintaining a dedicated DSL to write arbitrarily complex shaders might seem a bit overkill at first. However,
the benefit with this is that shader code also is multi-platform:

Besides OpenGL, kool also has a WebGPU backend, which requires shaders to be written in WGSL. Although WGSL
follows the same concepts as GLSL (and all the other shader languages as well), the syntax is very different.
By using a DSL, the same shader logic can be transformed into GLSL and WGSL. No need to maintain multiple shader
sources for different backends! This approach should even work for more shading languages, like, e.g., metal.

### Example KSL shader

Here's a minimal example for a custom KSL shader:

```kotlin
val customShader = KslShader("Hello world shader") {
    val interStageColor = interStageFloat4()
    vertexStage {
        main {
            val mvp = mvpMatrix()
            val localPosition = float3Var(vertexAttribFloat3(Attribute.POSITIONS))
            outPosition set mvp.matrix * float4Value(localPosition, 1f.const)
            interStageColor.input set vertexAttribFloat4(Attribute.COLORS)
        }
    }
    fragmentStage {
        main {
            colorOutput(interStageColor.output)
        }
    }
}
```

If you ever wrote a shader before the structure should be familiar: The shader consists of a vertex
stage (responsible for projecting the individual mesh vertices onto the screen) and a fragment stage (responsible
for computing the output-color for each pixel covered by the mesh). This example shader is almost as simple as a valid
shader can be: It uses a pre-multiplied MVP matrix to project the vertex position attribute to the screen. Moreover,
the color attribute is taken from the vertex input and forwarded to the fragment shader via `interStageColor`. The
fragment stage then simply takes the color from `interStageColor` and writes it to the screen.

A little more complex example is available in the [HelloKsl] demo.

### Compute shaders

So far, this chapter discussed only regular shaders used for drawing geometry. A different kind of shaders are compute shaders,
which can be used to offload compute workload to the GPU. kool and KSL also support compute shaders. Examples are available in the `HelloCompute` and `BeeShader` demos.

<seealso>
<category ref="related">
<a href="Compute-Shaders.md"></a>
</category>
</seealso>

[HelloCompute]: https://github.com/kool-engine/kool/blob/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/helloworld/HelloCompute.kt
[BeeShader]: https://github.com/kool-engine/kool/blob/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/bees/GpuBees.kt
[HelloKsl]: https://github.com/kool-engine/kool/blob/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/helloworld/HelloKsl.kt
