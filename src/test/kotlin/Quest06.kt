import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest06 {
    fun one(input: List<String>) = unique(input, false)

    fun two(input: List<String>) = unique(input, true)

    fun three(input: List<String>) = unique(input, true)

    private fun unique(input: List<String>, first: Boolean): String {
        val parents = mutableMapOf<String, MutableList<String>>()
        for (l in input) {
            val (k, v) = l.split(":")
            v.split(",").forEach { parents.getOrPut(it) { mutableListOf() }.add(k) }
        }
        val queue = ArrayDeque(parents["@"]!!.map { listOf(it) })
        val paths = mutableListOf<List<String>>()
        while (queue.isNotEmpty()) {
            val n = queue.removeFirst()
            for (p in parents[n.last()]!!) {
                if (p == "RR") {
                    paths.add(n + p)
                } else if (p !in n) {
                    queue.add(n + p)
                }
            }
        }
        val uni = paths.groupingBy { it.size }.eachCount().entries.first { it.value == 1 }.key
        return paths.first { it.size == uni }.reversed().map { if (first) it[0] else it }.joinToString("", postfix = "@")
    }
}

val Quest06Test by testSuite {
    val quest = "06"

    with(Quest06) {
        test("one") {
            val sample = """
                RR:A,B,C
                A:D,E
                B:F,@
                C:G,H
                D:@
                E:@
                F:@
                G:@
                H:@
            """.trimIndent().lines()
            one(sample) shouldBe "RRB@"

            val input = lines(quest, 1)
            one(input) shouldBe "RRNFPMRZDZWZ@"
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe "RJQNSSLDHS@"
        }

        test("three") {
            val input = lines(quest, 3)
            three(input) shouldBe "RFKJKXPZBNCF@"
        }
    }
}
