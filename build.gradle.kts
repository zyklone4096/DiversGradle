plugins {
    id("DiversGradle")
}

version = "1.0.0"

helldivers {
    author = "Example"

    arsenal {
//        forceOptions = true   // enable this to generate options for single package mod
    }

    pack("example") {
//        description = "test"  // for Arsenal manifest
    }
}
