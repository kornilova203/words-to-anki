import java.io.File

val pluralFormRegex = Regex(", ?-?(er|en|nen|e|n|s|\"e|#e|ñe|=|¨e|¨)(?=\$| \\()")
val nullPluralForms = Regex("(, -|,-|,•|,)$")
val correctNullPluralForm = Regex(", -$")
val kasus = Regex(" ?\\((an|zu|vor|für|über|auf|von) \\+ (D|A)\\.\\)")
val lineSplit = Regex("(?<=\\w)- (?=\\w)")

fun main() {
    val result = File("words-list.txt").readLines()
        .map { line ->
            val cleanLine = line
                .replace(". ..", "...")
                .replace(pluralFormRegex, ", -$1")
                .replace(", -#e", ", -¨e")
                .replace(", -\"e", ", -¨e")
                .replace(", -ñe", ", -¨e")
                .replace(", -=", ", -¨")
                .replace(lineSplit, "")
                .replace(nullPluralForms, ", -")


            val readyToTranslate = cleanLine
                .replace("|", "")
                .replace("(Sg.)", "")
                .replace(pluralFormRegex, "")
                .replace(correctNullPluralForm, "")
                .replace(kasus, "")

            Pair(cleanLine, readyToTranslate)
        }
        .filter { (_, readyToTranslate) -> !readyToTranslate.equals("andere wichtige Wörter und Wendungen", true) }
        .joinToString("\n") { (line, readyToTranslate) ->
            "$line;$readyToTranslate"
        }
    File("to-translate.csv").writeText(result)
}