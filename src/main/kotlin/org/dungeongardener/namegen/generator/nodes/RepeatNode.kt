package org.dungeongardener.namegen.generator.nodes

import org.dungeongardener.namegen.GeneratorContext
import org.dungeongardener.namegen.generator.ContentBuilder
import org.dungeongardener.util.numberexpr.NumExpr
import org.flowutils.random.RandomSequence

/**
 * Uses the same generator node to generate several content objects (or groups of them) in sequence.
 */
data class RepeatNode<T, R>(val times: NumExpr, val node: GeneratorNode<T, R>): GeneratorNode<T, R> {

    override fun generate(random: RandomSequence, context: GeneratorContext<T, R>, builder: ContentBuilder<T, R>) {
        val repeats = times.evaluate(context.numContext).toInt()

        for (i in 1 .. repeats) {
            node.generate(random, context, builder)
        }
    }
}