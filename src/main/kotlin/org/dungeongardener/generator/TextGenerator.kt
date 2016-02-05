package org.dungeongardener.generator

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.nodes.GeneratorNode
import org.dungeongardener.util.randomProvider
import org.flowutils.random.RandomSequence

/**
 *
 */
data class TextGenerator(val node: GeneratorNode<String, String>,
                         val context: GeneratorContext<String, String>) : Generator<String> {

    override fun generate(parameters: Map<String, Any>?, random: RandomSequence?): String {
        // TODO: Insert parameters as referable variables in the context passed in
        // TODO: Set random generator of the context as well
        return node.generate(randomProvider, context)
    }


}