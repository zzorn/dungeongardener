package org.dungeongardener

import org.dungeongardener.model.DungeonGardenerWorld
import java.io.File

/**
 * Main Entry point
 */
fun main(args: Array<String>) {
    println("Hello world")

    val world = DungeonGardenerWorld()
    world.load(File("src/main/res/defaultworld"))


}
