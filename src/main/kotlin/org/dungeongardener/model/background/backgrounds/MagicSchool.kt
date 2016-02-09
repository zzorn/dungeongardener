package org.dungeongardener.model.background.backgrounds

import org.dungeongardener.model.background.BackgroundCallback
import org.dungeongardener.model.background.OccupationBase
import org.dungeongardener.model.creature.Creature
import org.dungeongardener.model.skill.Skill
import org.dungeongardener.model.skill.Spell

/**
 *
 */
class MagicSchool(override val name: String,
                  override val description: String,
                  val magicTypeSkill: Skill,
                  val spellsInSchool: List<Spell>,
                  val skillsInSchool: List<Skill>,
                  val tuition: Double = 300.0): OccupationBase() {

    override fun enter(character: Creature, callback: BackgroundCallback) {
        var year = 1
        var scholarship = false

        character.basicStats.int
        callback.note("")


        node("Entrance test") {
            if (not test(INT)) end("Failed entrance test")
        }

        node("Try for scholarship") {
            if (veryHardTest(INT)) {
                scholarship = true
                msg("Got a scholarship")
            }
        }

        node("School Year") {
            if (not scholarship) node("Pay tuition") {
                if (not pay("Pay tuition", tuition)) {
                end("Declined to pay")
            }
            }

            if (year <= 3) node("Basic Studies") {
                learn("Magic Theory", 1d6 * 10)
                learn(
            }

            if (year > 3) node("Advanced Studies") {
                learn("Magic Theory", 1d6 * 5)

            }

        }
    }
}