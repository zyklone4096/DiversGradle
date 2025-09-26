plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("DiversGradle") {
            id = "DiversGradle"
            implementationClass = "dev.zyklone.diversgradle.DiversGradle"
        }
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}
