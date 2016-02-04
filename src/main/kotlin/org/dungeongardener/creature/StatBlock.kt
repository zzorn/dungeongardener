package org.dungeongardener.creature

import org.dungeongardener.util.numberexpr.NumExpr

/**
 * Describes the basic abilities and skills of a type of creature or person
 */
// TODO: Add skills, special abilities, etc.
data class StatBlock (
        val strength: NumExpr,
        val constitution: NumExpr,
        val dexterity: NumExpr,
        val intelligence: NumExpr,
        val wisdom: NumExpr,
        val charisma: NumExpr
) {
}