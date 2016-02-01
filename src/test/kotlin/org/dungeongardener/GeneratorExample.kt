package org.dungeongardener

import org.dungeongardener.namegen.TextGenerators

/**
 *
 */

fun main(args: Array<String>) {

    val generator = TextGenerators(
"""
DwarfName = "Rug", "Orof", 0.1:"Skolde", 3:"Dorin", 0.5:"Bimbur", 0.1:HumanName, 0.1:BananaName, 0.2:DwarfName + "-" + DwarfName
HumanName = "Bob", "Pete", 0.1:BananaName
BananaName = "Bana" + ("", 3:"na", 2:"nana") + "!"

""")

    println(generator.generate("DwarfName"))
    println(generator.generate("HumanName"))
}

