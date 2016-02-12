package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context
import org.dungeongardener.util.SimpleContext
import org.dungeongardener.util.parser.Language
import org.dungeongardener.util.parser.ParsingError
import org.flowutils.Check
import java.io.File
import java.util.*

/**
 * Used for importing definition files.
 */
class ImportContext(val basePath: File = File("./"),
                    val initialRegisteredLanguages: List<Language<out Definitions>> = emptyList()) {

    private val imports: MutableMap<Import, Definitions> = LinkedHashMap()
    private val languages: MutableMap<String, Language<out Definitions>> = LinkedHashMap()

    init {
        for (lang in initialRegisteredLanguages) {
            registerLanguage(lang)
        }
    }

    fun <T: Definitions>registerLanguage(language: Language<T>) {
        registerLanguage(language, language.extension.toString())
    }

    fun <T: Definitions>registerLanguage(language: Language<T>, extension: String) {
        Check.identifier(extension, "extension")
        languages.put(extension, language)
    }

    /**
     * Import the specified file
     */
    fun import(import: Import): Definitions {
        var definitions: Definitions? = imports.get(import)

        if (definitions == null) {
            // Parse definitions
            val file = import.getFile(basePath)
            val language = languages.get(import.type)
            if (language == null) throw ParsingError("Unknown language type '${import.type}' when trying to import '$file'")
            definitions = language.parseFirst(file)
            imports.put(import, definitions)

            // Load all imports used by the loaded definitions
            for (import in definitions.imports) {
                import(import)
            }
        }

        return definitions
    }

    /**
     * Import specified file and all its dependencies, and process it with the specified context.
     */
    fun process(import: Import, context: Context, processed: MutableSet<Import> = HashSet()) {
        if (processed.contains(import)) return

        processed.add(import)

        // Import
        val definitions = import(import)

        // Process dependencies
        for (dependencyImport in definitions.imports) {
            if (!processed.contains(dependencyImport)) {
                process(dependencyImport, context, processed)
            }
        }

        // Process self
        definitions.process(context)
    }

    /**
     * Load the specified import and all dependencies, and create a context with all definitions from the import.
     */
    fun createContext(importPath: String): Context {
        return createContext(Import(importPath))
    }

    /**
     * Load the specified import and all dependencies, and create a context with all definitions from the import.
     */
    fun createContext(import: Import): Context {
        val context = SimpleContext()
        process(import, context)
        return context
    }

    /**
     * Process all currently imported files with the specified context
     */
    fun process(context: Context) {
        // Process the last imported program first, as it should have least dependencies.
        // Although dependencies are anyway resolved when things are evaluated, so should mostly not matter
        for (program in imports.values.reversed()) {
            program.process(context)
        }
    }

    /**
     * Load all definitions that we can load and process them
     */
    fun loadAll(context: Context = SimpleContext()) : Context {
        // Load all
        loadAllInDir(basePath)

        // Process all
        process(context)

        return context
    }

    private fun loadAllInDir(dir: File) {
        for (file in dir.listFiles()) {

            // Recurse
            if (file.isDirectory && !file.isHidden && file.name != "." && file.name != "..") {
                loadAllInDir(file)
            }

            // Load any files that we can load
            if (!file.isDirectory && !file.isHidden) {
                for (language in languages.values) {
                    if (file.name.endsWith("." + language.extension)) {
                        val def = language.parseFirst(file)
                        imports.put(Import(file.relativeTo(basePath)), def)
                    }
                }
            }
        }
    }
}