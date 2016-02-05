package org.dungeongardener.model.creature

import org.dungeongardener.util.numberexpr.NumExpr

/**
 * Describes the basic abilities and skills of a type of creature or person
 */
data class StatBlock (
        val strength: NumExpr,
        val constitution: NumExpr,
        val dexterity: NumExpr,
        val intelligence: NumExpr,
        val willpower: NumExpr,
        val charisma: NumExpr
)