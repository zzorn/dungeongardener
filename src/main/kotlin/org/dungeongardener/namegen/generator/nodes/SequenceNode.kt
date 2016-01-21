package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.flowutils.random.RandomSequence

/**
 *
 */
data class SequenceNode<T, R>(val nodes: MutableList<GeneratorNode<T, R>>) : GeneratorNode<T, R> {
    override fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>) {
        for (n in nodes) n.generate(random, context, builder)
    }
}