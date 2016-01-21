package org.dungeongardener.namegen

import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.namegen.generator.nodes.GeneratorNode
import org.dungeongardener.util.randomnumber.NumContext
import org.dungeongardener.util.randomnumber.SimpleNumContext
import java.io.File
import java.util.*

/**
 * Combines syllables or words from a file to create names or other strings.
 */
class TextGenerators(val markovText: String? = null): GeneratorContext<String, String> {

    constructor(markovFile: File) : this(markovFile.readText())

    private val generators: MutableMap<String, GeneratorNode<String, String>> = LinkedHashMap()

    init {
        if (markovText != null) parseGenerators(markovText)
    }

    override val numContext : NumContext = SimpleNumContext()

    val readOnlyGenerators: Map<String, GeneratorNode<String, String>>
        get() = generators

    fun addGenerator(generatorId: String, generator: GeneratorNode<String, String>) {
        generators.put(generatorId, generator)
    }

    fun parseGenerators(generatorMarkupText: String) {
        // Parse the generator markup

        // TODO
    }

    override fun getGenerator(generatorId: String): GeneratorNode<String, String>? {
        return generators.get(generatorId)
    }

    class TextContextBuilder(val builder: StringBuilder = StringBuilder()): ContentBuilder<String, String> {
        override fun add(content: String) {
            builder.append(content)
        }

        override fun build(): String {
            return builder.toString()
        }
    }

    override fun createContentBuilder(): ContentBuilder<String, String> {
        return TextContextBuilder()
    }
}