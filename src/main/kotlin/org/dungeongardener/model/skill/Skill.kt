package org.dungeongardener.model.skill

import org.dungeongardener.model.creature.Attribute

/**
 *
 */
data class Skill(val name: String,
                 val description: String,
                 val baseStat: Attribute,
                 val difficulty: Double = 1.0) {
}