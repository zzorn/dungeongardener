package org.dungeongardener.namegen.generator.text

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.WeightedMap
import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.namegen.generator.nodes.*
import org.dungeongardener.util.Context
import org.dungeongardener.util.SimpleContext
import org.dungeongardener.util.parser.Multiplicity.ONE_OR_MORE
import org.dungeongardener.util.parser.Multiplicity.ZERO_OR_MORE
import org.dungeongardener.util.parser.parsers.*
import org.dungeongardener.util.parser.result.ParseSuccess
import org.flowutils.Check
import java.io.File
import java.util.*

/**
 * Combines syllables or words from a file to create names or other strings.
 */
class TextGenerators(val markovText: String? = null): GeneratorContext<String, String> {

    constructor(markovFile: File) : this(markovFile.readText())

    private val functions: MutableMap<String, (String) -> String> = LinkedHashMap()

    fun addFunction(name: String, function: (String) -> String) {
        Check.identifier(name, "name")
        functions.put(name, function)
    }

    private val generators: MutableMap<String, GeneratorNode<String, String>> = LinkedHashMap()

    init {
        addFunction("capitalize") {it.capitalize()}
        addFunction("lowerCase") {it.toLowerCase()}
        addFunction("upperCase") {it.toUpperCase()}

        if (markovText != null) parseGenerators(markovText)
    }

    override val context: Context = SimpleContext()

    val readOnlyGenerators: Map<String, GeneratorNode<String, String>>
        get() = generators

    fun addGenerator(generatorId: String, generator: GeneratorNode<String, String>) {
        generators.put(generatorId, generator)
    }

    fun parseGenerators(generatorMarkupText: String) {
        // Parse the generator markup

        // Primitives
        val comment = (+"#" + CharParser("\n", ZERO_OR_MORE, negated = true) + "\n").named("comment")
        val ws = ZeroOrMore(AnyOf(comment, CharParser(" \t\n", ONE_OR_MORE))).named("whitespace")
        val letter = CharParser('a'..'z', 'A'..'Z').named("letter")
        val zeroOrMoreLettersAndNumbers = CharParser(ZERO_OR_MORE, 'a'..'z', 'A'..'Z', '0'..'9')
        val identifier = SequenceParser(letter, zeroOrMoreLettersAndNumbers).named("identifier")
        val quotedString = SequenceParser(+"\"", CharParser("\"", ZERO_OR_MORE).anyExcept().generatesMatchedText(), +"\"").named("quotedString")
        val positiveNumber = SequenceParser(
                CharParser(ONE_OR_MORE, '0'..'9'),
                Optional(+".", CharParser(ONE_OR_MORE, '0'..'9'))
        ).named("positiveNumber").generates { it.text.toDouble() }

        // Generators
        val generator = LazyParser()
        val lowLevelGenerator = LazyParser()
        val lowLevelGenerator2 = LazyParser()
        val parens = SequenceParser(+"(", ws, generator, ws, +")", ws).named("parenthesis")
        val text = quotedString.generates {
            val content = it.pop<String>()
            ContentNode<String, String>(content)
        }.named("text")
        val weight = AnyOf(SequenceParser(positiveNumber, ws, +":"), AutoMatch().generates { 1.0 }).named("weight") // Default to 1.0 weight if weight not specified
        val weightedNode = SequenceParser(ws, weight, ws, lowLevelGenerator).named("weightedNode").generates {
            Pair<GeneratorNode<String, String>, Double>(it.pop(), it.pop())
        }
        val weighted = SequenceParser(weightedNode, OneOrMore(ws, +",", ws, weightedNode)).named("weightedSet").generates {
            val weightedMap = WeightedMap<GeneratorNode<String, String>>()
            val popCurrentNodeResults = it.popCurrentNodeResults<Pair<GeneratorNode<String, String>, Double>>()
            for ((node, w) in popCurrentNodeResults) {
                weightedMap.addEntry(node, w)
            }
            WeightedNode(weightedMap)
        }
        val concatenated = SequenceParser(lowLevelGenerator2, OneOrMore(ws, +"+", ws, lowLevelGenerator2)).named("concatenated").generates {
            SequenceNode(it.popCurrentNodeResults<GeneratorNode<String, String>>())
        }
        val reference = identifier.generates { ReferenceNode<String, String>(it.text) }.named("reference")

        val function = SequenceParser(AnyOf(functions.keys.map{it.toString()}).generatesMatchedText(), ws, +"(", ws, generator, ws, +")", ws).generates {
            val node = it.pop<GeneratorNode<String, String>>()
            val functionName = it.pop<String>()
            TextFunctionNode(node, functions.get(functionName) ?: throw IllegalStateException("Could not find function $functionName"))
        }

        lowLevelGenerator2.parser = SequenceParser(AnyOf(parens, text, function, reference), ws)
        lowLevelGenerator.parser = SequenceParser(AnyOf(concatenated, lowLevelGenerator2), ws)
        generator.parser = SequenceParser(AnyOf(weighted, lowLevelGenerator), ws).named("generator")

        // Named generators
        val namedGenerator = SequenceParser(identifier.generatesMatchedText(), ws, +"=", ws, generator.cut()).named("namedGenerator").generates {
            val id: String = it.pop<String>(1)
            val gen: GeneratorNode<String, String> = it.pop()
            Pair(id, gen)
        }
        val generatorParser = SequenceParser(
                ws,
                ZeroOrMore(
                        namedGenerator
                ),
                ws,
                EndOfInput()
        )

        val result = generatorParser.parse(generatorMarkupText)
        if (result is ParseSuccess) {
            for (g in result.results) {
                generators += g as Pair<String, GeneratorNode<String, String>>
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