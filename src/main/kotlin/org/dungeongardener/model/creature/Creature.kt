package org.dungeongardener.model.creature

import org.dungeongardener.model.ability.Ability
import org.dungeongardener.model.skill.Skill
import java.util.*

/**
 * A specific individual creature, npc, or player character entity.
 */
data class Creature(
        val type: CreatureType,
        val modifications: Set<CreatureModification> = emptySet(),
        var name: String? = null,
        var nickName: String? = null,
        var surName: String? = null,
        val basicAttributes: BasicAttributes = BasicAttributes(),
        val weight_kg: Double = 0.0,
        val length_m: Double = 0.0,
        var age_years: Double = 0.0,
        val factions: MutableSet<String> = LinkedHashSet(),
        val skills: MutableMap<Skill, Double> = HashMap(),
        val abilities: MutableSet<Ability> = LinkedHashSet(),
        var maxHitpoints: Double = 0.0,
        var maxMana: Double = 0.0,
        var hitpoints: Double = maxHitpoints,
        var mana: Double = maxMana,
        val history: MutableList<HistoryEntry> = ArrayList()) {


    fun addSkillExp(skill: Skill, skillExp: Double) {
        skills.put(skill, skill.calculateValueWithAddedExp(skills.get(skill) ?: 0.0, skillExp))
    }

}