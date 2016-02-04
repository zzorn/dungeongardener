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
dwarfNameWithTitle = GeneratedDwarfName + (3:(" " + title), "")
DwarfName = 10: dwarfNameWithTitle, "Rug", "Orof", 0.1:"Skolde", 1:"Dorin", 0.5:"Bimbur", 0.1:HumanName+"son", 0.1:BananaName, 0.4:GeneratedDwarfName + "-" + dwarfNameWithTitle

## Human names
HumanName = (1:"Bob", 1:"Pete", 0.1:BananaName, 0.2: DwarfName) + (0.4:(" " + title), "")

title = (
        emotion,
        verb + " " + species,
        species + " " + species,
        "the " + emotion,
        "the " + species,
        2: "the " + verb,
        3: species + " " + verb,
        2: "the " + emotion + " " + species,
        2: "the " + emotion + " " + verb + " of " + species + "s",
        "the " + emotion + " " + verb,
        "the " + emotion + (" but ", " and ", " ") + emotion + " " + verb,
        "the " + emotion + " " + species
)

emotion = "hopeless", "grim", "happy", "glorious", "sad", "laughing", "wonderful", "huge", "fallow", "shallow", "rageful", "angry", "suspicious", "virtuous"
species = "orc", "troll", "gnome", "dwarf", "elf", "rabbit", "bear", "wolf", "deer", "eagle", "sparrow", "hawk", "salmon", "dragon", "wyrm", "snake", "oak", "tree", "birch", "dog", "cat"
verb = "slayer", "killer", "bringer", "lover", "talker", "crusher", "stalker", "breaker", "builder", "befriender", "seer", "runner", "sitter", "eater", "hunter", "hugger"


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

