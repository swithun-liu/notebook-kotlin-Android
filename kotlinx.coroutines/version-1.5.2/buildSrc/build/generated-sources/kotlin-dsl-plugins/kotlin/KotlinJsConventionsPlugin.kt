/**
 * Precompiled [kotlin-js-conventions.gradle.kts][Kotlin_js_conventions_gradle] script plugin.
 *
 * @see Kotlin_js_conventions_gradle
 */
class KotlinJsConventionsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Kotlin_js_conventions_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
