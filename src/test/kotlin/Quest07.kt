import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

object Quest07 {
    private fun parse(input: List<String>) = input.map { it.split(":") }.associate { (k, v) -> k to v.replace(",", "") }

    fun one(input: List<String>): String {
        val data = parse(input)
        val values = mutableMapOf<String, Int>()
        for ((k, ops) in data.entries) {
            var v = 10
            var s = 0
            repeat(10) { r ->
                s += when (ops[r % ops.length]) {
                    '+' -> ++v
                    '-' -> --v
                    else -> v
                }
            }
            values[k] = s
        }
        return values.entries.sortedByDescending { it.value }.joinToString("") { it.key }
    }

    fun two(input: List<String>, trackData: List<String>): String {
        val data = parse(input)
        val lx = trackData[0].length - 1
        val ly = trackData.size - 1
        val track = trackData[0].drop(1) +
                (1..<ly).map { trackData[it][lx] }.joinToString("") +
                trackData[ly].reversed() +
                (0..<ly).map { trackData[it][0] }.reversed().joinToString("")
        val plans = data.entries.associate { (plan, ops) ->
            plan to track.withIndex()
                .map { (i, c) -> if (c == '=' || c == 'S') ops[i % ops.length] else c }.joinToString("")
        }
        val essences = buildMap {
            for ((plan, ops) in plans.entries) {
                var v = 10
                put(plan, (0..<(track.length * 10)).sumOf {
                    when (ops[it % ops.length]) {
                        '+' -> ++v
                        '-' -> --v
                        else -> v
                    }
                })
            }
        }
        essences.forEach { (p, e) -> println("$p: $e") }
        println("-".repeat(10))
        return essences.entries.sortedByDescending { it.value }.joinToString("") { it.key }
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest07Test by testSuite {
    val quest = "07"

    with(Quest07) {
        test("one") {
            val sample = """
                A:+,-,=,=
                B:+,=,-,+
                C:=,-,+,+
                D:=,=,=,+
            """.trimIndent().lines()
            one(sample) shouldBe "BDCA"

            val input = lines(quest, 1)
            one(input) shouldBe "AJCDIBFGH"
        }

        test("two") {
            val sampleData = """
                A:+,-,=,=
                B:+,=,-,+
                C:=,-,+,+
                D:=,=,=,+
            """.trimIndent().lines()
            val sampleTrack = """
                S+===
                -   +
                =+=-+
            """.trimIndent().lines()
            two(sampleData, sampleTrack) shouldBe "DCBA"

            val input = lines(quest, 2)
            val track = """
                S-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--
                -                                                                     -
                =                                                                     =
                +                                                                     +
                =                                                                     +
                +                                                                     =
                =                                                                     =
                -                                                                     -
                --==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-
            """.trimIndent().lines()
            two(input, track) shouldNotBe "BEDJGIFHK"
        }

        test("three") {
            val sample = """
            """.trimIndent().lines()
            three(sample) shouldBe 0

//            val input = lines(quest, 3)
//            three(input) shouldBe 0
        }
    }
}
