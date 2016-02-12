package org.dungeongardener.model.skill

import org.dungeongardener.model.creature.Attribute

/**
 *
 */
open class Skill(val name: String,
                 val baseStat: Attribute,
                 val difficulty: Double = 1.0,
                 val description: String,
                 var category: SkillCategory? = null) {

    /**
     * Calculate the skill value after the specified amount of experience is added to it.
     */
    fun calculateValueWithAddedExp(existingSkillValue: Double, addedExp: Double): Double {
        var skillBase = Math.floor(existingSkillValue).toInt()
        val previousExp = getUnusedExp(existingSkillValue)
        var exp = previousExp + addedExp
        while (exp >= expCostForSkillLevel(skillBase + 1)) {
            skillBase += 1
            exp -= expCostForSkillLevel(skillBase)
        }
        val expToNext = expCostForSkillLevel(skillBase + 1)
        val skillFraction = exp / expToNext
        return skillBase + skillFraction
    }

    fun expCostForSkillLevel(skillValue: Int): Double {
        return difficulty * skillCostFunction(skillValue)
    }

    fun getUnusedExp(skillValue: Double): Double {
        var skillBase = Math.floor(skillValue).toInt()
        val skillFraction = skillValue - skillBase
        return skillFraction * expCostForSkillLevel(skillBase + 1)
    }

    /**
     * Function used to determine difficulty of skill progression.  Basically cost at level x is x^2 / 100, rounded up.
     */
    private fun skillCostFunction(skillValue: Int): Int = Math.ceil(skillValue * skillValue / 100.0).toInt()

    override fun toString(): String{
        return "${category?.name ?: ""}: $name ($baseStat $difficulty) $description"
    }


}


