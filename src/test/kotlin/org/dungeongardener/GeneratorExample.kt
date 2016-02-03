package org.dungeongardener

import org.dungeongardener.namegen.generator.text.TextGenerators

/**
 *
 */

fun main(args: Array<String>) {

    val generator = TextGenerators(
"""
## Dwarfy names
DwarfNameComponent = "zu", "bu", "ra", "mi", "gu", "zh", 2:"rr", "ka", "ko", "ju", "ba", "be", "go", "lin", "li", "si", "sin"
GeneratedDwarfName = capitalize(DwarfNameComponent + DwarfNameComponent + (0.5:DwarfNameComponent, "") + (0.3:DwarfNameComponent, "") + (0.2:DwarfNameComponent, "") + (0.1:DwarfNameComponent, ""))
DwarfName = 10: GeneratedDwarfName, "Rug", "Orof", 0.1:"Skolde", 3:"Dorin", 0.5:"Bimbur", 0.1:HumanName+"son", 0.1:BananaName, 0.2:DwarfName + "-" + DwarfName

## Human names
HumanName = 1:"Bob", 1:"Pete", 0.1:BananaName, 0.2: DwarfName

## Banana names
BananaName = "Ba" + (1:"", 3:"na", 2:"nana", 3: lowerCase(BananaName)) + "!"
""")

    generateAndPrint(generator, "DwarfName")
    generateAndPrint(generator, "HumanName")
    generateAndPrint(generator, "BananaName")
}

private fun generateAndPrint(generator: TextGenerators, type: String, count: Int = 25) {
    println("${type}s:")
    for (i in 1..count) println("  " + generator.generate(type))
}

