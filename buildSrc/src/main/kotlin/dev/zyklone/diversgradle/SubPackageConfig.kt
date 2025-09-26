package dev.zyklone.diversgradle

class SubPackageConfig(name: String, val main: MainConfig) {
    var dir: String = name
    var description: String? = null
}
