import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.pow

object Quest03 {
    private fun parse(input: List<String>) = input.map { it.substringAfter('-').split(" ") }

    private fun calc(s: String, v: String): Long {
        val ss = s.repeat(v.length / s.length + 1).takeLast(v.length)
        return v.indices.sumOf { v[it].digitToInt() * ss[it].digitToInt().toDouble().pow(v.lastIndex - it).toLong() }
    }

    fun one(input: List<String>): Long {
        return parse(input).sumOf { (s, v) -> calc(s, v) }
    }

    fun two(input: List<String>): Long {
        return parse(input).sumOf { (s, v) -> calc(s, v) }
    }

    fun three(input: List<String>): Long {
        return parse(input).sumOf { (s, v) ->
            val r = calc (s, v)
            var valid = true
            for (o in v.toLong() - 1 downTo 1L) {
                if (calc(s, o.toString()) == r) {
                    valid = false
                    break
                }
            }
            if (valid) r else 0L
        }
    }
}

val Quest03Test by testSuite {
    val quest = "03"

    with(Quest03) {
        test("one") {
            val sample = """
                ISO-3210 7654
                ISO-8742 2468
                ISO-1111 1111
            """.trimIndent().lines()
            one(sample) shouldBe 1478

            val input = lines(quest, 1)
            one(input) shouldBe 11799
        }

        test("two") {
            val sample = """
                ISO-3210 7654
                ISO-8742 2468
                ISO-1 1111
                ISO-13 777
                ISO-34 140856
                ISO-789 2465112
                ISO-2341 945
            """.trimIndent().lines()
            two(sample) shouldBe 1161463L

            val input = lines(quest, 2)
            two(input) shouldBe 18489920877L
        }

        test("three") {
            val sample = """
                ISO2-3 23
                ISO2-61 149
                ISO2-978 99
                ISO2-5887 5923
                ISO2-76988 123749
                ISO2-435657 929977
                ISO2-3949 870737
            """.trimIndent().lines()
            three(sample) shouldBe 50987L

            val input = lines(quest, 3)
            three(input) shouldBe 358940L
        }
    }
}
