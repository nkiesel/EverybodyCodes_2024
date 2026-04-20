import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest11 {
    private fun parse(input: List<String>) = input.map { it.split(":", ",") }.associate { it[0] to it.drop(1) }

    fun one(input: List<String>) = exec(parse(input), "A", 4).toInt()

    fun two(input: List<String>) = exec(parse(input), "Z", 10).toInt()

    fun three(input: List<String>): Long {
        val termites = parse(input)
        return termites.keys.map { exec(termites, it, 20) }.let { it.max() - it.min() }
    }

    private fun exec(termites: Map<String, List<String>>, start: String, rep: Int): Long {
        var population = CountingMap(listOf(start))
        repeat(rep) {
            val nc = CountingMap<String>()
            population.keys.forEach { key ->
                val amount = population.count(key)
                termites[key]!!.forEach { nc.inc(it, amount) }
            }
            population = nc
        }
        return population.values().sum()
    }
}

val Quest11Test by testSuite {
    val quest = "11"

    with(Quest11) {
        test("one") {
            val sample = """
                A:B,C
                B:C,A
                C:A
            """.trimIndent().lines()
            one(sample) shouldBe 8

            val input = lines(quest, 1)
            one(input) shouldBe 37
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 215579
        }

        test("three") {
            val sample = """
                A:B,C
                B:C,A,A
                C:A
            """.trimIndent().lines()
            three(sample) shouldBe 268815L

            val input = lines(quest, 3)
            three(input) shouldBe 1584568766426L
        }
    }
}
