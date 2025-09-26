# DiversGradle

Package **Helldivers 2** mods with Gradle

## Requirements

- JDK 17 or later installed with environment variables configured
  - [Temurin](https://adoptium.net/zh-CN/temurin/releases?version=17)
- Editor with `*.gradle.kts` support
  - [Visual Studio Code](https://code.visualstudio.com/)
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)

## Tasks
- Package for generic mod managers (Helldivers2ModManager, HD2 Arsenal etc.): `gradlew packageSimple`
- Package for HD2 Arsenal (with manifest and sub options support): `gradlew packageArsenal`

## Usage

See `build.gradle.kts`
