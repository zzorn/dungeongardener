package org.dungeongardener.model

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.background.BackgroundLanguage
import org.dungeongardener.model.background.activities.Activity
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.creature.CreatureType
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.model.skill.SkillCategory
import org.dungeongardener.model.skill.SkillLanguage
import org.dungeongardener.util.SimpleContext
import org.dungeongardener.util.genlang.GenLang
import org.dungeongardener.util.genlang.nodes.ImportContext
import org.flowutils.random.XorShift
import java.io.File
import java.util.*

/**
 *
 */
class DungeonGardenerWorld() : World {

    lateinit var skillCategories: SkillCategory
    override val skills: MutableMap<String, Skill> = LinkedHashMap()
    override val creatureTypes: MutableMap<String, CreatureType> = LinkedHashMap()

    fun load(directory: File) {
        val importContext = ImportContext(directory, listOf(GenLang(), SkillLanguage(), BackgroundLanguage()))

        // Load and process everything
        val context = importContext.loadAll()

        // Load skills
        val skillLang = SkillLanguage()
        skillCategories = skillLang.parseFirst(File(directory, "skills.skills"))
        skillCategories.storeInMap(skills)

        // DEBUG:
        println("\nSkills")
        for (skill in skills.values) {
            println(skill)
        }

        // DEBUG: Run background activity
        val vagabondActivity = context.getReference<Activity>("Vagabond")

        val callback: BackgroundCallback = object : BackgroundCallback {
            val random = XorShift()

            override fun note(message: String) {
                println("Note $message")
            }

            override fun pay(message: String, gold: Double): Boolean {
                println("Pay $message $gold")
                return true
            }

            override fun skillExp(skill: Skill, exp: Double) {
                println("Skill exp ${skill.name} $exp")
            }

            override fun selectActivity(activities: List<Activity>): Activity {
                println("Select activity $activities")
                return random.nextElement(activities)
            }

            override fun enteredActivity(activity: Activity) {
                println("Entered activity $activity")
            }
        }

        vagabondActivity.enter(Creature(CreatureType("Murderhobo")), callback, SimpleContext(), this)

    }

}