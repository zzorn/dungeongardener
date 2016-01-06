package org.dungeongardener.namegen

import org.flowutils.random.RandomSequence

/**
 *
 */
interface TextGenerator {

    fun generate(random: RandomSequence, builder: StringBuilder)

    fun generate(random: RandomSequence): String {
        val builder = StringBuilder()
        generate(random, builder)
        return builder.toString()
    }

}