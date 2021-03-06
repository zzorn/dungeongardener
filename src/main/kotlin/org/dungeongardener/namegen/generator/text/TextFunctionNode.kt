package org.dungeongardener.namegen.generator.text

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.namegen.generator.nodes.GeneratorNode
import org.flowutils.random.RandomSequence

/**
 * Applies a string based function to the result generated by the input node.
 */
class TextFunctionNode(val node: GeneratorNode<String, String>, val textFunction: (String) -> String) : GeneratorNode<String, String> {
    override fun generate(random: RandomSequence, context: GeneratorContext<String, String>, builder: ContentBuilder<String, String>) {
        builder.add(textFunction(node.generate(random, context)))
    }
}