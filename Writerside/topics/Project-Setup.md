<link-summary>Get started with kool using code.</link-summary>
<card-summary>Learn how to set up a kool project.</card-summary>

# Project Setup

Setting up a new project can get complicated, especially when targeting both web and desktop platforms. We recommend starting from the [kool templates](%url.kool.templates%) project.


## Dependencies

<tabs>
<tab title="Gradle">
<p>build.gradle.kts</p>
<code-block lang="kotlin">
dependencies {
    implementation("de.fabmax.kool:kool-core:%latest_version%")
}
</code-block>
</tab>
<tab title="Version Catalog">
<p>libs.versions.toml</p>
<code-block lang="toml">
[versions]
kool = "%latest_version%"

[libraries]
kool-core = { id = "de.fabmax.kool:kool-core", version.ref = "kool" }
</code-block>

<p>build.gradle.kts</p>

<code-block lang="kotlin">
dependencies {
    implementation(libs.kool.core)
}
</code-block>
</tab>
</tabs>


## Main class

Example setup:

```kotlin
fun main() = KoolApplication {
    // Create and add a new scene
    addScene {
        // Add a mouse manipulatable camera
        defaultOrbitCamera()

        // Add a spinning colored cube
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

        // Add a directional light to illuminate the scene
        lighting.singleDirectionalLight {
            setup(Vec3f(-1f, -1f, -1f))
            setColor(Color.WHITE, 5f)
        }
    }
}
```
