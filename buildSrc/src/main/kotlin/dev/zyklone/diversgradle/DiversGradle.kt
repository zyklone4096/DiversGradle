package dev.zyklone.diversgradle

import org.gradle.api.Plugin
import org.gradle.api.Project

open class DiversGradle: Plugin<Project> {
    override fun apply(target: Project) {
        val config = target.extensions.create("helldivers", MainConfig::class.java, target)
    }
}