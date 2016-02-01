package org.dungeongardener.namegen

import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.namegen.generator.nodes.*
import org.dungeongardener.util.parser.Multiplicity.ONE_OR_MORE
import org.dungeongardener.util.parser.Multiplicity.ZERO_OR_MORE
import org.dungeongardener.util.parser.parsers.*
import org.dungeongardener.util.parser.result.ParseSuccess
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

        // Primitives
        val ws = CharParser(" \t\n", ZERO_OR_MORE)
        val letter = CharParser('a'..'z', 'A'..'Z')
        val zeroOrMoreLettersAndNumbers = CharParser(ZERO_OR_MORE, 'a'..'z', 'A'..'Z', '0'..'9')
        val identifier = Sequence(letter, zeroOrMoreLettersAndNumbers)
        val quotedString = Sequence("\"".unaryPlus(), CharParser("\"", ZERO_OR_MORE).anyExcept().generatesMatchedText(), "\"".unaryPlus())
        val positiveNumber = Sequence(
                CharParser(ONE_OR_MORE, '0'..'9'),
                Optional(+".", CharParser(ONE_OR_MORE, '0'..'9'))
        ).generates { it.text.toDouble() }

        // Generators
        val generator = LazyParser()
        val parens = Sequence("(".unaryPlus(), ws, generator, ws, ")".unaryPlus(), ws)
        val text = quotedString.generates {
            val content = it.pop<String>()
            ContentNode<String, String>(content)
        }
        val weight = AnyOf(Sequence(positiveNumber, ws, +":"), AutoMatch().generates { 1.0 }) // Default to 1.0 weight if weight not specified
        val weightedNode = Sequence(ws, weight, ws, generator).generates {
            Pair<GeneratorNode<String, String>, Double>(it.pop(), it.pop())
        }
        val weighted = Sequence(weightedNode, OneOrMore(weightedNode)).named("weighted").generates {
            val weightedMap = WeightedMap<GeneratorNode<String, String>>()
            for ((node, w) in it.popCurrentNodeResults<Pair<GeneratorNode<String, String>, Double>>()) {
                weightedMap.addEntry(node, w)
            }
            WeightedNode(weightedMap)
        }
        val concatenated = Sequence(generator, OneOrMore(ws, +"+", ws, generator)).generates {
            SequenceNode(it.popCurrentNodeResults<GeneratorNode<String, String>>())
        }
        val reference = identifier.generates { ReferenceNode<String, String>(it.text) }

        generator.parser = Sequence(AnyOf(weighted, concatenated, parens, text, reference), ws)

        // Named generators
        val namedGenerator = Sequence(identifier.generatesMatchedText(), ws, "=".unaryPlus(), ws, generator).generates {
            val id: String = it.pop<String>(1)
            val gen: GeneratorNode<String, String> = it.pop()
            Pair(id, gen)
        }
        val generatorParser = Sequence(
                ws,
                ZeroOrMore(
                        namedGenerator
                ),
                EndOfInput()
        )

        val result = generatorParser.parse(generatorMarkupText)
        if (result is ParseSuccess) {
            for (g in result.results) {
                generators += g as Pair<String,GeneratorNode<String, String>>
            }
        }
        else {
            throw IllegalArgumentException("Problem parsing generator: " + result)
        }
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