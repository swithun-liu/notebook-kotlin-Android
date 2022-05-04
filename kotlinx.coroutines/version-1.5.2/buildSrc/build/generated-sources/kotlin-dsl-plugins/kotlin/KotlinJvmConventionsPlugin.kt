/**
 * Precompiled [kotlin-jvm-conventions.gradle.kts][Kotlin_jvm_conventions_gradle] script plugin.
 *
 * @see Kotlin_jvm_conventions_gradle
 */
class KotlinJvmConventionsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Kotlin_jvm_conventions_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
