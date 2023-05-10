import kotlin.math.abs
import kotlin.random.Random

object PasswordGenerator {
    private val NUMBERS_ALPHABET: Array<Char> = arrayOf(
        '0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9'
    )

    private val CAPITAL_ENGLISH_ALPHABET: Array<Char> = arrayOf(
        'A', 'B', 'C', 'D',
        'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X',
        'Y', 'Z'
    )

    private val SMALL_ENGLISH_ALPHABET: Array<Char> = arrayOf(
        'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l',
        'm', 'n', 'o', 'p',
        'q', 'r', 's', 't',
        'u', 'v', 'w', 'x',
        'y', 'z'
    )

    private val CAPITAL_RUSSIAN_ALPHABET: Array<Char> = arrayOf(
        'А', 'Б', 'В', 'Г',
        'Д', 'Е', 'Ё', 'Ж',
        'З', 'И', 'Й', 'К',
        'Л', 'М', 'Н', 'О',
        'П', 'Р', 'С', 'Т',
        'У', 'Ф', 'Х', 'Ц',
        'Ч', 'Ш', 'Щ', 'Ъ',
        'Ы', 'Ь', 'Э', 'Ю',
        'Я'
    )

    private val SMALL_RUSSIAN_ALPHABET: Array<Char> = arrayOf(
        'а', 'б', 'в', 'г',
        'д', 'е', 'ё', 'ж',
        'з', 'и', 'й', 'к',
        'л', 'м', 'н', 'о',
        'п', 'р', 'с', 'т',
        'у', 'ф', 'х', 'ц',
        'ч', 'ш', 'щ', 'ъ',
        'ы', 'ь', 'э', 'ю',
        'я'
    )

    private val SINGS_ALPHABET: Array<Char> = arrayOf(
        '`', '~', '!', '@',
        '#', '$', '%', '^',
        '&', '*', '(', ')',
        '_', '+', '"', '№',
        ';', ':', '?', '-',
        '=', '/', '\\', '<',
        '>', '.', ',', '{',
        '}', '[', ']', '\''
    )

    private fun getAlphabetsUsed(passwordConfig: PasswordConfig): List<Array<Char>> {
        val alphabetsUsed = mutableListOf<Array<Char>>()

        val useNumbersAlphabet = passwordConfig.useNumbersAlphabet
        val useCapitalEnglishAlphabet = passwordConfig.useCapitalAlphabet
        val useSmallEnglishAlphabet = true
        val useCapitalRussianAlphabet = passwordConfig.useCapitalAlphabet && passwordConfig.useRussianAlphabet
        val useSmallRussianAlphabet = passwordConfig.useRussianAlphabet
        val useSingsAlphabet = passwordConfig.useSingsAlphabet

        if (useNumbersAlphabet) alphabetsUsed.add(NUMBERS_ALPHABET)
        if (useCapitalEnglishAlphabet) alphabetsUsed.add(CAPITAL_ENGLISH_ALPHABET)
        if (useSmallEnglishAlphabet) alphabetsUsed.add(SMALL_ENGLISH_ALPHABET)
        if (useCapitalRussianAlphabet) alphabetsUsed.add(CAPITAL_RUSSIAN_ALPHABET)
        if (useSmallRussianAlphabet) alphabetsUsed.add(SMALL_RUSSIAN_ALPHABET)
        if (useSingsAlphabet) alphabetsUsed.add(SINGS_ALPHABET)

        return alphabetsUsed
    }

    private fun getRandomizer(length: Int, login: String?, target: String?): Random {
        val loginHashCode = login.hashCode()
        val targetHashCode = target.hashCode()
        return if (loginHashCode == 0 || targetHashCode == 0) Random else {
            val seed = loginHashCode and targetHashCode xor length.inv()
            Random(seed)
        }
    }

    fun generate(passwordConfig: PasswordConfig): String {
        val alphabetsUsed = getAlphabetsUsed(passwordConfig)
        val randomizer = getRandomizer(
            length = passwordConfig.length,
            login = passwordConfig.login,
            target = passwordConfig.target,
        )

        val passwordBuilder = StringBuilder()
        var passwordIsCorrect = false
        while (!passwordIsCorrect) {
            var numberCount = 0
            var englishCount = 0
            var russianCount = 0
            var capitalCount = 0
            var singsCount = 0

            for (index in 0 until passwordConfig.length) {
                val alphabetIndex = randomizer.nextInt(alphabetsUsed.size)
                val alphabet = alphabetsUsed[alphabetIndex]

                if (alphabet === NUMBERS_ALPHABET) {
                    numberCount++
                }
                if (alphabet === CAPITAL_ENGLISH_ALPHABET) {
                    englishCount++
                    capitalCount++
                }
                if (alphabet === SMALL_ENGLISH_ALPHABET) {
                    englishCount++
                }
                if (alphabet === CAPITAL_RUSSIAN_ALPHABET) {
                    russianCount++
                    capitalCount++
                }
                if (alphabet === SMALL_RUSSIAN_ALPHABET) {
                    russianCount++
                }
                if (alphabet === SINGS_ALPHABET) {
                    singsCount++
                }

                val charIndex = randomizer.nextInt(alphabet.size)
                val char = alphabet[charIndex]
                passwordBuilder.append(char)
            }

            if (
                numberCount >= passwordConfig.minNumbersCount &&
                singsCount >= passwordConfig.minSingsCount &&
                capitalCount >= passwordConfig.minCapitalCount &&
                abs(russianCount - englishCount) >= passwordConfig.minMultiLanguageCount
            ) passwordIsCorrect = true else passwordBuilder.clear()
        }

        return passwordBuilder.toString()
    }
}