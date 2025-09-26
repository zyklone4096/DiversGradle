package dev.zyklone.diversgradle.arsenal

import dev.zyklone.diversgradle.MainConfig

class ArsenalConfig(val main: MainConfig) {
    var iconPath: String? = null
    var forceOptions: Boolean = false

    init {
        val task = this.main.project.tasks.register("packageArsenal", ArsenalPackageTask::class.java, this)
        this.main.packageTask.get().dependsOn(task)
    }
}
