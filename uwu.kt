import java.util.Locale
import kotlin.random.Random
// import kotlin.system.measureTimeMillis

private var stutterChance = 20
private var emojiChance = 50

fun main(args: Array<String>) {
    if(args.size == 2) {
        stutterChance = args[0].toInt()
        emojiChance = args[1].toInt()
        println("Stutter chance: $stutterChance%")
        println("Emoji chance: $emojiChance%")
    }

    print("Input: ")
    val text = readLine()!!
    println("Here ya go ^-^:")
    println(uwuify(text))
    // println(measureTimeMillis { uwuify(text) })
}

private fun uwuify(input: String): String {
    var output = input

    // replace some words
    var find = output.findAnyOf(words.keys, ignoreCase = true)
    while (find != null) {
        val word = output.substring(find.first, find.first + find.second.length) // because find.second is always lowercase

        var replace = words[find.second]!!

        if(!word.toCharArray().any { it.isLowerCase() }) // all caps
            replace = replace.uppercase()
        else if(word[0].isUpperCase()) // first char is uppercase
            replace = replace.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } // capitalize

        output = output.replace(word, replace)

        find = output.findAnyOf(words.keys, ignoreCase = true)
    }

    // nya-ify
    output = output.replace("na", "nya")
    output = output.replace("Na", "Nya")
    output = output.replace("nA", "nyA")
    output = output.replace("NA", "NYA")

    // replace l and r with w
    output = output.replace('l', 'w')
    output = output.replace('r', 'w')
    output = output.replace('L', 'W')
    output = output.replace('R', 'W')

    // stutter sometimes
    var offset = 0
    for(s in output.split(" ")) {
        if(s.length > 1 && randomWithChance(stutterChance)) {
            output = output.prefixWord(s, "${s[0]}-", offset)
        }
        offset += s.length
    }

    // add a text emoji at the end sometimes
    if(!punctuation.contains(output.last()) && randomWithChance(emojiChance)) {
        output += emojis[Random.nextInt(0, emojis.size-1)]
    }

    // add a text emoji after punctuation sometimes
    val array = output.toCharArray()
    for((eOffset, char) in array.withIndex()) {
        val index = array.indexOf(char)
        if(punctuation.contains(char) && (index == array.size-1 || array[index+1] == ' ') // ', ' or '! ' etc or last character of the input because I don't want emojis in text.like.this
            && randomWithChance(emojiChance)) {
            output = output.suffixChar(char, emojis[Random.nextInt(0, emojis.size-1)], eOffset)
        }
    }

    return output
}

private fun String.prefixWord(word: String, prefix: String, startIndex: Int = 0) = substring(0, indexOf(word, startIndex)) + prefix + substring(indexOf(word, startIndex))

private fun String.suffixChar(char: Char, suffix: String, startIndex: Int = 0) = substring(0, indexOf(char, startIndex) + 1) + suffix + substring(indexOf(char, startIndex) + 1)

private fun randomWithChance(chance: Int): Boolean = Random.nextInt(1, 101) <= chance

private val words = mapOf(
    Pair("small", "smol"),
    Pair("cute", "kawaii~"),
    Pair("fluff", "floof"),
    Pair("love", "luv"),
    Pair("stupid", "baka"),
    Pair("what", "nani"),
    Pair("meow", "nya~"),
)

private val emojis = listOf(
    " rawr x3",
    " OwO",
    " UwU",
    " o.O",
    " -.-",
    " >w<",
    " (⑅˘꒳˘)",
    " (ꈍᴗꈍ)",
    " (˘ω˘)",
    " (U ᵕ U❁)",
    " σωσ",
    " òωó",
    " (U ﹏ U)",
    " ʘwʘ",
    " :3",
    " XD",
    " nyaa~~",
    " mya",
    " >_<",
    " rawr",
    " ^^",
    " (^•ω•^)",
    " (✿oωo)",
    " („ᵕᴗᵕ„)",
    " (。U⁄ ⁄ω⁄ ⁄ U。)"
)

private val punctuation = listOf(',', '.', '!', '?')