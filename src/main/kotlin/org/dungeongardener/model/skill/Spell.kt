package org.dungeongardener.model.skill

import org.dungeongardener.model.creature.Attribute

/**
 *
 */
class Spell(name: String,
            description: String,
            difficulty: Double = 1.0) : Skill(name,  description, Attribute.INT, difficulty) {
}