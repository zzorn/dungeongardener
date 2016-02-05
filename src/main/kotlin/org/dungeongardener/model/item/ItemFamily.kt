package org.dungeongardener.model.item

import org.dungeongardener.model.Family

/**
 * Used for item classification
 */
class ItemFamily(name: String, val acceptableModifiers: Set<ItemModifier>) : Family<ItemType>(name) {

}