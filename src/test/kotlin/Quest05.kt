import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest05 {
    private fun parse(input: List<String>): List<MutableList<Int>> {
        val rows = input.map { it.ints() }
        val columns = List(rows[0].size) { mutableListOf<Int>() }
        for (row in rows) {
            for ((i, n) in row.withIndex()) {
                columns[i].add(n)
            }
        }
        return columns
    }

    fun one(input: List<String>): Int {
        val columns = parse(input)
        val cols = columns.size
        repeat(10) { r ->
            val n = columns[r % cols].removeAt(0)
            val x = (r + 1) % cols
            var y = 0
            var dy = 1
            repeat(n - 1) {
                y += dy
                if (y !in columns[x].indices) {
                    dy = -dy
                    y += dy
                }
            }
            columns[x].add(y + if (dy == 1) 0 else 1, n)
        }
        return columns.map { it[0] }.joinToString("").toInt()
    }

    fun two(input: List<String>): Long {
        val columns = parse(input)
        val cols = columns.size
        var r = 0
        val counts = CountingMap<Int>()
        while (true) {
            val n = columns[r % cols].removeAt(0)
            val x = (r + 1) % cols
            var y = 0
            var dy = 1
            repeat(n - 1) {
                y += dy
                if (y !in columns[x].indices) {
                    dy = -dy
                    y += dy
                }
            }
            columns[x].add(y + if (dy == 1) 0 else 1, n)
            val number = columns.map { it[0] }.joinToString("").toInt()
            counts.inc(number)
            r++
            if (counts.count(number) == 2024L) {
                return r.toLong() * number
            }
        }
    }

    fun three(input: List<String>): String {
        val columns = parse(input)
        val cols = columns.size
        var r = 0
        val counts = CountingMap<String>()
        while (true) {
            val n = columns[r % cols].removeAt(0)
            val x = (r + 1) % cols
            var y = 0
            var dy = 1
            repeat(n - 1) {
                y += dy
                if (y !in columns[x].indices) {
                    dy = -dy
                    y += dy
                }
            }
            columns[x].add(y + if (dy == 1) 0 else 1, n)
            val number = columns.map { it[0] }.joinToString("")
            counts.inc(number)
            if (counts.entries().any { it.value > 40L }) {
                return counts.entries().maxBy { it.key }.key
            }
            r++
        }
    }
}

val Quest05Test by testSuite {
    val quest = "05"

    with(Quest05) {
        test("one") {
            val sample = """
                2 3 4 5
                3 4 5 2
                4 5 2 3
                5 2 3 4
            """.trimIndent().lines()
            one(sample) shouldBe 2323

            val s2 = """
                2 3 4 5
                6 7 8 9
            """.trimIndent().lines()
            one(s2) shouldBe 6254

            val input = lines(quest, 1)
            one(input) shouldBe 2233
        }

        test("two") {
            val sample = """
                2 3 4 5
                6 7 8 9
            """.trimIndent().lines()
            two(sample) shouldBe 50877075L

            val input = lines(quest, 2)
            two(input) shouldBe 22208631112470L
        }

        test("three") {
            val sample = """
                2 3 4 5
                6 7 8 9
            """.trimIndent().lines()
            three(sample) shouldBe "6584"

            val input = lines(quest, 3)
            three(input) shouldBe "9049100210031000"
        }
    }
}
