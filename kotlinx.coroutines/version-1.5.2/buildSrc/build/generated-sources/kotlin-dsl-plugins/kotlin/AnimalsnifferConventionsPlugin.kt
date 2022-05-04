/**
 * Precompiled [animalsniffer-conventions.gradle.kts][Animalsniffer_conventions_gradle] script plugin.
 *
 * @see Animalsniffer_conventions_gradle
 */
class AnimalsnifferConventionsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Animalsniffer_conventions_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
