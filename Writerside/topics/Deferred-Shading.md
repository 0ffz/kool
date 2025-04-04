# Deferred Shading

> The deferred rendering pipeline is somewhat deprecated at the moment and will probably change significantly
in the future. Documentation is therefore very limited.

{ style="warning" }

So far, all discussed shaders use traditional forward rendering. Another option to do the rendering is deferred
shading which can be cheaper and allows for more advanced lighting effects.

To use deferred shading, you need to use an appropriate shader. Currently, the only builtin option is [`DeferredKslPbrShader`][DeferredKslPbrShader], which has mostly the same configuration options like the
forward-rendering version described [above](Builtin-Shaders.md#kslpbrshader).

Examples using deferred shading are [DeferredDemo], [ReflectionDemo] and the
[VehicleDemo]

[DeferredDemo]: https://github.com/kool-engine/kool/blob/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/DeferredDemo.kt
[DeferredKslPbrShader]: https://github.com/kool-engine/kool/blob/main/kool-core/src/commonMain/kotlin/de/fabmax/kool/pipeline/deferred/DeferredPbrShader.kt
[ReflectionDemo]: https://github.com/kool-engine/kool/blob/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/ReflectionDemo.kt
[VehicleDemo]: https://github.com/kool-engine/kool/tree/main/kool-demo/src/commonMain/kotlin/de/fabmax/kool/demo/physics/vehicle
