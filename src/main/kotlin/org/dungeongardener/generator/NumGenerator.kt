package org.dungeongardener.generator

import org.dungeongardener.util.Context
import org.dungeongardener.util.numberexpr.NumExpr
import org.flowutils.random.RandomSequence

/**
 *
 */
data class NumGenerator(val expr: NumExpr, val context: Context): Generator<Double> {
    override fun generate(parameters: Map<String, Any>?, random: RandomSequence?): Double {

        // TODO: Insert parameters as referable variables in the context passed in
        // TODO: Set random generator of the context as well

        return expr.evaluate(context)
    }
}