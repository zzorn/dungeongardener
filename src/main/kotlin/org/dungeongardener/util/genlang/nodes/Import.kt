package org.dungeongardener.util.genlang.nodes

import java.io.File

/**
 *
 */
data class Import(val packagePath: List<String>) {

    constructor(path: String) : this(path.split('.'))

    constructor(relativeFile: File) : this(relativeFile.path.split(File.pathSeparator, "."))

    val type: String = packagePath.last()

    fun getFile(basePath: File): File {
        return File(basePath, packagePath.subList(0, packagePath.size - 1).joinToString("/") + "." + packagePath.last())
    }


}