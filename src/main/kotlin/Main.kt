fun main(args: Array<String>) {
    val config  = PasswordConfig(
        length = 150
    )
    val password = PasswordGenerator.generate(config)
    println(password)
}