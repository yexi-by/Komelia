package snd.komelia.ui.strings

@kotlinx.serialization.Serializable
data class ValidationStrings(
    val alreadyContainsBook: String,
    val alreadyContainsSeries: String,
    val collectionAlreadyExists: String,
    val failedToParseUrl: String,
    val invalidWebhookUrl: String,
    val newPasswordRequired: String,
    val passwordsMustMatch: String,
    val presetAlreadyExists: String,
    val readListAlreadyExists: String,
    val required: String,
)
