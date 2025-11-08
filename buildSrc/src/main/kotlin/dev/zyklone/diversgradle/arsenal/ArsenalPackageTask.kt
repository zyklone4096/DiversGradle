package dev.zyklone.diversgradle.arsenal

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.FileNotFoundException
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.notExists
import kotlin.io.path.readBytes
import kotlin.io.path.relativeTo
import kotlin.io.path.walk

abstract class ArsenalPackageTask @Inject constructor(@Internal val config: ArsenalConfig): DefaultTask() {
    init {
        this.group = "helldivers"
    }

    @OptIn(ExperimentalPathApi::class)
    @TaskAction
    fun action() {
        val main = this.config.main
        val project = main.project
        val logger = project.logger
        val root = project.layout.projectDirectory.asFile.toPath()
        val dest = project.layout.buildDirectory.dir("dist").get()
        val version = project.version
        val packages = main.packages

        if (packages.isEmpty())
            return
        dest.asFile.let {
            if (!it.exists())
                it.mkdirs()
        }

        // generate manifest
        val obj = JsonObject()
        obj.addProperty("Version", 1)
        obj.addProperty("Guid", UUID.nameUUIDFromBytes("DiversGradle:${project.name}:${main.author}".toByteArray()).toString())
        obj.addProperty("Name", project.name)
        obj.addProperty("Description", main.description.get())

        val destStream = dest.file("${project.name}-$version-arsenal.zip").asFile.outputStream()
        val zip = ZipOutputStream(destStream)

        config.iconPath?.let {
            // copy icon
            val icon = root.resolve(it)
            if (icon.notExists() || !icon.isRegularFile())
                throw FileNotFoundException("No icon file $icon")
            obj.addProperty("IconPath", it)
            zip.putNextEntry(ZipEntry(it))
            zip.write(icon.readBytes())
            zip.closeEntry()
        }

        if (config.forceOptions || packages.size > 1) {   // use options
            logger.info("Creating package with options")

            val options = JsonArray()
            for ((name, config) in packages) {
                logger.info("Copying option $name")
                val sub = "opt_$name"
                val pkgDir = root.resolve(config.dir)
                for (path in pkgDir.walk()) {
                    if (!path.isDirectory()) {
                        zip.putNextEntry(ZipEntry("$sub/${path.relativeTo(pkgDir)}"))
                        zip.write(path.readBytes())
                        zip.closeEntry()
                    }
                }

                // add to manifest
                val opt = JsonObject()
                opt.addProperty("Name", name)
                opt.add("Include", JsonArray().apply {
                    add(sub)
                })
                config.description?.let { opt.addProperty("Description", it) }
                options.add(opt)
            }
            obj.add("Options", options)
        } else {
            logger.info("Creating simple package")

            // copy files
            val (_, config) = packages.toList().single()
            val pkgDir = root.resolve(config.dir)
            for (path in pkgDir.walk()) {
                if (!path.isDirectory()) {
                    zip.putNextEntry(ZipEntry(path.relativeTo(pkgDir).toString()))
                    zip.write(path.readBytes())
                    zip.closeEntry()
                }
            }
        }

        // put manifest
        val gson = GsonBuilder().setPrettyPrinting().create()
        zip.putNextEntry(ZipEntry("manifest.json"))
        zip.write(gson.toJson(obj).toByteArray())
        zip.closeEntry()

        zip.close()
        destStream.close()
    }
}