package org.dungeongardener.model.creature

import org.dungeongardener.model.creature.Attribute.*

/**
 * Contains a set of basic attributes.
 */
data class BasicAttributes(
        var str: Double = 0.0,
        var dex: Double = 0.0,
        var int: Double = 0.0,
        var wil: Double = 0.0,
        var car: Double = 0.0
) {


    fun changeAttribute(attribute: Attribute, amount: Double) {
        when (attribute) {
            STR -> str += amount
            DEX -> dex += amount
            INT -> int += amount
            WIL -> wil += amount
            CAR -> car += amount
        }
    }
}