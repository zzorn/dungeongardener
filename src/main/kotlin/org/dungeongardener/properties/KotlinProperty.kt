package org.dungeongardener.properties

import org.flowutils.Symbol
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KType
import kotlin.reflect.jvm.javaType

/**
 *
 */
// TODO: Check that kotlin property getters and setters work as assumed (taking host obj as parameter)
class KotlinProperty<T>(val kotlinProperty: KMutableProperty<T>,
                        override val defaultValue: T? = null) : Property<T> {

    override val name: Symbol = Symbol.get(kotlinProperty.name)
    override val desc: String? = (kotlinProperty.annotations.find { it is Desc} as? Desc)?.description

    override val type: KType = kotlinProperty.getter.returnType
    override val javaType: Class<T> = kotlinProperty.getter.returnType.javaType as Class<T>

    override fun getValue(host: Any): T = kotlinProperty.getter.call(host)
    override fun setValue(host: Any, value: T) = kotlinProperty.setter.call(host, value)

    override val range: Range? = kotlinProperty.annotations.find { it is Range} as Range?
    override val hidden: Boolean = kotlinProperty.annotations.any { it is Hidden}

    override fun toString(): String {
        return "$name: ${type.toString()} = $defaultValue // $desc"
    }

}
