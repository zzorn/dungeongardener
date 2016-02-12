package org.dungeongardener.model.skill

import org.dungeongardener.util.Context
import org.dungeongardener.util.genlang.nodes.Definitions
import org.dungeongardener.util.genlang.nodes.Import
import java.util.*

/**
 * Skills organized in a category hierarchy
 */
class SkillCategory(val name: String) : Definitions {

    val skills: MutableList<Skill> = ArrayList()
    val subCategories: MutableList<SkillCategory> = ArrayList()
    var parentCategory: SkillCategory? = null

    fun addSubCategory(sub: SkillCategory) {
        sub.parentCategory = this
        subCategories.add(sub)
    }

    fun addSubCategory(name: String): SkillCategory {
        val subCategory = SkillCategory(name)
        addSubCategory(subCategory)
        return subCategory
    }

    fun addSkill(skill: Skill) {
        skill.category = this
        skills.add(skill)
    }

    fun storeInMap(skillMap: MutableMap<String, Skill> = HashMap()): Map<String, Skill> {
        for (skill in skills) {
            skillMap.put(skill.name, skill)
        }

        for (subCategory in subCategories) {
            subCategory.storeInMap(skillMap)
        }

        return skillMap
    }


    override val imports: List<Import> = emptyList()

    override fun process(context: Context) {
        context.setReference("skills", storeInMap())
    }
}