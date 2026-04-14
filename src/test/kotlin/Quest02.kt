import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest02 {
    private fun parse(input: List<String>) = input[0].substringAfter(':').split(',') to input.drop(2)

    fun one(input: List<String>): Int {
        val (words, texts) = parse(input)
        return words.map { Regex.fromLiteral(it) }.sumOf { it.findAll(texts[0]).count() }
    }

    fun two(input: List<String>): Int {
        val (words, texts) = parse(input)
        var count = 0
        for (text in texts) {
            val cm = mutableSetOf<Int>()
            for (word in words + words.map { it.reversed() }) {
                var pos = -1
                while (true) {
                    pos = text.indexOf(word, pos + 1)
                    if (pos == -1) break
                    cm += (pos..<(pos + word.length))
                }
            }
            count += cm.size
        }
        return count
    }

    fun three(input: List<String>): Int {
        val (words, texts) = parse(input)
        val cm = mutableSetOf<Pair<Int, Int>>()
        for ((row, t) in texts.withIndex()) {
            for (word in words + words.map { it.reversed() }) {
                val text = t + t.substring(0, word.length - 1)
                var pos = -1
                do {
                    pos = text.indexOf(word, pos + 1)
                    if (pos != -1) (pos..<(pos + word.length)).forEach { cm += (it % t.length) to row }
                } while (pos != -1)
            }
        }
        val verticals = texts[0].indices.map { i -> texts.map { it[i] }.joinToString("") }
        for ((col, text) in verticals.withIndex()) {
            for (word in words + words.map { it.reversed() }) {
                var pos = -1
                do {
                    pos = text.indexOf(word, pos + 1)
                    if (pos != -1) (pos..<(pos + word.length)).forEach { cm += col to it }
                } while (pos != -1)
            }
        }
        return cm.size
    }
}

val Quest02Test by testSuite {
    val quest = "02"

    with(Quest02) {
        test("one") {
            val sample1 = """
                WORDS:THE,OWE,MES,ROD,HER

                AWAKEN THE POWER ADORNED WITH THE FLAMES BRIGHT IRE
            """.trimIndent().lines()
            one(sample1) shouldBe 4

            val sample2 = """
                WORDS:THE,OWE,MES,ROD,HER

                THE FLAME SHIELDED THE HEART OF THE KINGS
            """.trimIndent().lines()
            one(sample2) shouldBe 3

            val sample3 = """
                WORDS:THE,OWE,MES,ROD,HER

                POWE PO WER P OWE R:
            """.trimIndent().lines()
            one(sample3) shouldBe 2

            val sample4 = """
                WORDS:THE,OWE,MES,ROD,HER

                THERE IS THE END
            """.trimIndent().lines()
            one(sample4) shouldBe 3

            val input = lines(quest, 1)
            one(input) shouldBe 33
        }

        test("two") {
            val sample = """
                WORDS:THE,OWE,MES,ROD,HER,QAQ

                AWAKEN THE POWE ADORNED WITH THE FLAMES BRIGHT IRE
                THE FLAME SHIELDED THE HEART OF THE KINGS
                POWE PO WER P OWE R
                THERE IS THE END
                QAQAQ
            """.trimIndent().lines()
            two(sample) shouldBe 42

            val input = lines(quest, 2)
            two(input) shouldBe 5079
        }

        test("three") {
            val sample = """
                WORDS:THE,OWE,MES,ROD,RODEO

                HELWORLT
                ENIGWDXL
                TRODEOAL
            """.trimIndent().lines()
            three(sample) shouldBe 10

            val input = lines(quest, 3)
            three(input) shouldBe 11887
        }
    }
}
