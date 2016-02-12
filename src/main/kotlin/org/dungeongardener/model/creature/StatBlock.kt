package org.dungeongardener.model.creature

import org.dungeongardener.util.genlang.nodes.Expression

/**
 * Describes the basic attributes and skills of a type of creature or person
 */
data class StatBlock (
        val strength: Expression,
        val dexterity: Expression,
        val intelligence: Expression,
        val willpower: Expression,
        val charisma: Expression
)