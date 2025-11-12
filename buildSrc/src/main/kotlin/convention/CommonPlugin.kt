package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        // dependency conflict // todo
    }

}