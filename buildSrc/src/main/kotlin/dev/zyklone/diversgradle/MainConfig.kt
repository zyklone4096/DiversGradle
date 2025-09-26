package dev.zyklone.diversgradle

import dev.zyklone.diversgradle.arsenal.ArsenalConfig
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.provider.Property
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.invoke

abstract class MainConfig(val project: Project) {
    abstract val description: Property<String>

    private var arsenal: ArsenalConfig? = null

    internal val simple: TaskProvider<PackageSimpleTask> =
        project.tasks.register("packageSimple", PackageSimpleTask::class.java, this)
    internal val packageTask: TaskProvider<Task> =
        project.tasks.register("package") {
            this.group = "build"
            this.dependsOn(simple)
        }

    internal val packages = HashMap<String, SubPackageConfig>()

    var author: String? = null

    init {
        this.description.convention("")
    }

    fun arsenal(action: Action<ArsenalConfig>) {
        if (this.arsenal == null) {
            this.arsenal = ArsenalConfig(this)
        }
        action.invoke(this.arsenal!!)
    }

    fun pack(name: String, action: Action<SubPackageConfig>) {
        action.invoke(this.packages.getOrPut(name) {
            SubPackageConfig(name, this)
        })
    }
}
