package org.dungeongardener.model.skill

import org.dungeongardener.model.creature.Attribute
import org.dungeongardener.util.parser.LanguageBase
import org.dungeongardener.util.parser.Parser

/**
 *
 */
class SkillLanguage() : LanguageBase<SkillCategory>("skills") {

    val baseStat = any(
            keyword("str", true).generates { Attribute.STR },
            keyword("dex", true).generates { Attribute.DEX },
            keyword("int", true).generates { Attribute.INT },
            keyword("wil", true).generates { Attribute.WIL },
            keyword("car", true).generates { Attribute.CAR }
    ).named("baseStat")

    val skill = ((+"*") - oneOrMoreCharsExcept("(").generatesTrimmedMatchedText() + "(" - baseStat + double - ")" - quotedString + ws).generates {
        Skill(it.pop(3), it.pop(2), it.pop(1), it.pop())
    }.named("skill")

    val skillBlock = lazy

    val category = (keyword("category", true) - oneOrMoreCharsExcept("{").generatesTrimmedMatchedText() + skillBlock).generates {
        createSkillCategory(it.pop(1), it.pop())
    }.named("category")

    val skills = (keyword("skills") + skillBlock).generates {
        createSkillCategory("skills", it.pop())
    }.named("skills")

    override val parser: Parser = ws + skills + endOfInput

    init {
        skillBlock.parser = (+"{" - zeroOrMore(any(category, skill)) + "}" + ws).generatesContentList().named("skillBlock")
    }

    private fun createSkillCategory(name: String, entries: List<Any>): SkillCategory {
        val skillCategory = SkillCategory(name)
        for (entry in entries) {
            when (entry) {
                is Skill -> skillCategory.addSkill(entry)
                is SkillCategory -> skillCategory.addSubCategory(entry)
                else -> throw IllegalArgumentException("Unknown skill entry $entry")
            }
        }
        return skillCategory
    }
}