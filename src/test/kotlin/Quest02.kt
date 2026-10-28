import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest02 {
    private fun parse(input: List<String>) = input.map { l ->
        l.split(" ").map { s ->
            Regex("""(\d+)([<>])(\d+)""").matchEntire(s)!!.groupValues.let { v -> m[v[2]]!!.map { v[it].toInt() } }
        }.toSet()
    }

    private val m = mapOf(">" to [3, 1], "<" to [1, 3])

    private fun connect(comparison: Set<List<Int>>, s: Int, t: Int, i: Int): Boolean {
        val o = if (i == 0) 1 else 0
        var n = setOf(s)
        while (true) {
            n = comparison.filter { it[i] in n }.map { it[o] }.toSet()
            if (t in n) return true
            if (n.isEmpty() || s in n) return false
        }
    }

    private fun checksum(comparison: Set<List<Int>>) = comparison.flatten().distinct()
        .sortedWith(Comparator { a, b ->
            when {
                [a, b] in comparison -> 1
                [b, a] in comparison -> -1
                else -> when {
                    connect(comparison, a, b, 0) -> 1
                    connect(comparison, a, b, 1) -> -1
                    connect(comparison, b, a, 0) -> -1
                    connect(comparison, b, a, 1) -> 1
                    else -> error("no connection between $a and $b in $comparison")
                }
            }
        })
        .joinToString("").toLong()

    fun one(input: List<String>): Long {
        return parse(input).sumOf { checksum(it) }
    }

    fun two(input: List<String>) = one(input)

    fun three(input: List<String>) = one(input)
}

val Quest02Test by testSuite {
    val quest = "02"

    with(Quest02) {
        test("one") {
            val sample = """
        4<3 2<5 3>1 1>2 1<5 4<1 3>2 4<2 3>5 4<5
        4>1 2>1 3>2 1<5 4>5 2<5 3>1 4>3 2<4 5<3
        5>1 5>2 4>1 2<4 3>2 3<4 1>2 3>5 1<3 4>5
    """.trimIndent().lines()
            one(sample) shouldBe 122157L

            val input = lines(quest, 1)
            one(input) shouldBe 401415L
        }

        test("two") {
            val sample = """
                2<5 3>1 1>2 1<5 4<1 3>2 4<2 3>5 4<5
                2>1 3>2 1<5 4>5 2<5 3>1 4>3 2<4 5<3
                5>1 5>2 4>1 3>2 3<4 1>2 3>5 1<3 4>5
            """.trimIndent().lines()
            two(sample) shouldBe 122157L

            val input = lines(quest, 2)
            two(input) shouldBe 56260760895L
        }

        test("three") {
            val sample = """
                3>1 1>2 1<5 4<2 3>5
                2>1 2<5 3>1 4>3 2<4 5<3
                5>1 3<4 1>2 3>5
            """.trimIndent().lines()
            three(sample) shouldBe 122157L

            val input = lines(quest, 3)
            three(input) shouldBe 599890842882L
        }
    }
}
