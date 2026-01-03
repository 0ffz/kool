package de.fabmax.kool.modules.compose


@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message =
        "This is internal API for Kool's compose ui implementation that may change without warning."
)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
@Retention(AnnotationRetention.BINARY)
annotation class InternalKoolComposeAPI
