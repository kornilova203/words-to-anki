import java.io.File

val pluralFormRegex = Regex("(, ?-?| -)(er|en|nen|e|n|s|\"e|#e|\\*er|\"er|ñe|=|¨e|¨er|¨|\"hen|5)(?=\$| \\()")
val nullPluralForms = Regex("(, -|,-|,•|,)$")
val correctNullPluralForm = Regex(", -$")
val kasus = Regex(" ?\\(((an|zu|vor|für|über|auf|von) )?\\+ (D|A|G)\\.\\)")
val lineSplit = Regex("(?<=\\w)- (?=\\w)")
val spaces = Regex(" +")

fun main() {
    val result = File("words-list.txt").readLines()
        .map { line ->
            val cleanLine = line
                .replace(". ..", "...")
                .replace(pluralFormRegex, ", -$2")
                .replace(", -#e", ", -¨e")
                .replace(", -\"e", ", -¨e")
                .replace(", -ñe", ", -¨e")
                .replace(", -=", ", -¨")
                .replace(", -\"hen", ", -nen")
                .replace(", -*er", ", -¨er")
                .replace(", -5", ", -s")
                .replace(lineSplit, "")
                .replace(nullPluralForms, ", -")


            val readyToTranslate = cleanLine
                .replace("|", "")
                .replace("(Sg.)", "")
                .replace(pluralFormRegex, "")
                .replace(correctNullPluralForm, "")
                .replace(kasus, "")
                .replace(spaces, " ")
                .replace(", -e/-s", "")

            Pair(cleanLine, readyToTranslate)
        }
        .filter { (_, readyToTranslate) -> !readyToTranslate.equals("andere wichtige Wörter und Wendungen", true) }
        .joinToString("\n") { (line, readyToTranslate) ->
            "$line;$readyToTranslate"
        }
    File("to-translate.csv").writeText(result)
}