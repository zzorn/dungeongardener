package org.dungeongardener.model.background.activities

/**
 *
 */
abstract class SimpleActivity(): Activity {
    override val name: String
        get() = this.toString()

}