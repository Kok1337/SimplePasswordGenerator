data class PasswordConfig(
    val length: Int = 16,

    val useRussianAlphabet: Boolean = true,
    val useCapitalAlphabet: Boolean = true,
    val useSingsAlphabet: Boolean = true,
    val useNumbersAlphabet: Boolean = true,

    val minNumbersCount: Int = 1,
    val minSingsCount: Int = 1,
    val minCapitalCount: Int = 1,
    val minMultiLanguageCount: Int = 1,

    val login: String? = null,
    val target: String? = null,
)