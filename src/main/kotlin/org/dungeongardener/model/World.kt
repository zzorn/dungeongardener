package org.dungeongardener.model

import org.dungeongardener.model.creature.CreatureType
import org.dungeongardener.model.skill.Skill

/**
 * Dungeon Gardener world model.
 */
interface World {

    val skills: Map<String, Skill>
    val creatureTypes: Map<String, CreatureType>

    fun getSkill(skillName: String): Skill = skills.get(skillName) ?: throw IllegalArgumentException("No skill named '$skillName' found")

}