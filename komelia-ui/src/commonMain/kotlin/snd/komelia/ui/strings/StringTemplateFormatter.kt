package snd.komelia.ui.strings

private const val escapedPercentSentinel = "\u0000komelia_percent\u0000"
private val namedPlaceholderRegex = Regex("\\{([A-Za-z0-9_]+)}")

internal fun String.formatTemplate(vararg arguments: Pair<String, Any?>): String {
    val values = arguments.associate { (name, value) -> name to (value?.toString() ?: "") }
    var result = replace("%%", escapedPercentSentinel)

    result = namedPlaceholderRegex.replace(result) { match ->
        val key = match.groupValues[1]
        values[key] ?: error("Missing template argument '$key' for '$this'")
    }

    check("%s" !in result && "%d" !in result) {
        "Legacy positional template placeholders are not supported anymore: '$this'"
    }

    return result.replace(escapedPercentSentinel, "%")
}
