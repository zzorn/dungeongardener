package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.WeightedMap
import org.dungeongardener.namegen.generator.ContentBuilder
import org.flowutils.random.RandomSequence

/**
 * Adds one content object randomly by its weight.
 */
data class WeightedNode<T, R>(val entries: WeightedMap<GeneratorNode<T, R>>): GeneratorNode<T, R> {

    override fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>) {
        entries.randomEntry(random).generate(random, context, builder)
    }

}