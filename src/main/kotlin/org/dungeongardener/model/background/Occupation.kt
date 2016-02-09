package org.dungeongardener.model.background

import org.dungeongardener.model.creature.Creature

/**
 * Something that a character can spend time in as part of their background.
 */
interface Occupation {

    val name: String
    val description: String
    /* val entranceRequirements:
     Can be things like:
        * roll under stat
        * roll under probability
        * pay x
        * roll under x or pay y
        * (not) a previous occupation
        * (not) a specific race
        * roll under skill
        * has stat over (or under) x
        * has skill? over (or under) x
        * social status is/over/under x
        * age over/under x
        * combinations of the above with boolean logic
        TODO: Create requirement interface that gets passed a creature and returns true or false, or asks for payment/resource use acceptance
      */

    /**
     * This includes first trying to pass the entrance, then looping for as many years as desired or until kicked out or dead.
     * Calls the callback with reports of what changes are being done to the creature,
     * for history entries to report to users or record with NPCs, and with decisions for
     * the player or npc AI to do.
     * Decisions should include some recommended options for NPC AI:s (and maybe players) to take, perhaps with weights to allow randomization.
     *
     * Instead of having NPC AI choose, the choice could be given to the occupations, by specifying NPC personality
     * traits such as risk adverseness and willingness to spend wealth, as well as a skill/spell preference function.
     *
     * TODO: Could also use a game world parameter, as NPC contacts are created.  Although they could be fleshed out later.
     *       If the gamemaster and the players used the same seed, they could have the same NPC contacts generated..  Although probably better to do it interactively with the GM.
     *
     */
    fun enter(character: Creature, callback: BackgroundCallback)

}