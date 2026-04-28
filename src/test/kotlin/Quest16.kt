import com.sun.beans.introspect.PropertyInfo
import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import sun.awt.geom.Curve.prev
import kotlin.math.max

object Quest16 {
    fun one(input: List<String>): String {
        val spins = input[0].ints()
        val wheels = List(input.size) { mutableListOf<String>() }
        for (line in input.drop(2)) {
            for (i in spins.indices) {
                val faces = if (line.length > i * 4) line.substring(i * 4, i * 4 + 3) else ""
                if (faces.isNotBlank()) wheels[i].add(faces)
            }
        }
        val indexes = IntArray(spins.size) { 0 }
        var line = ""
        repeat(100) {
            for (i in spins.indices) {
                indexes[i] = (indexes[i] + spins[i]) % wheels[i].size
            }
           line = indexes.indices.joinToString(" ") { wheels[it][indexes[it]] }
        }
        return line
    }

    fun two(input: List<String>, required: Long = 202420242024L): Long {
        val spins = input[0].ints()
        val wheels = List(input.size) { mutableListOf<String>() }
        for (line in input.drop(2)) {
            for (i in spins.indices) {
                val faces = if (line.length > i * 4) line.substring(i * 4, i * 4 + 3) else ""
                if (faces.isNotBlank()) wheels[i].add("${faces[0]}${faces[2]}")
            }
        }
        val indexes = IntArray(spins.size) { 0 }
        var total = 0L
        val indexLines = mutableListOf<String>()
        val totals = mutableListOf<Long>()
        var round = 0L
        var notJumped = true
        while (round < required) {
            for (i in spins.indices) {
                indexes[i] = (indexes[i] + spins[i]) % wheels[i].size
            }
            val line = indexes.indices.joinToString("") { wheels[it][indexes[it]] }
            val won = line.groupingBy { it }.eachCount().values.sumOf { max(0, it - 2) }
            if (notJumped) {
                val il = indexes.joinToString(",")
                val prev = indexLines.indexOf(il)
                if (prev == -1) {
                    totals += total
                    indexLines += il
                } else {
                    val d = indexLines.lastIndex - prev + 1
                    val wd = total - totals[prev]
                    val m = (required - round) / d
                    round += m * d
                    total += m * wd
                    notJumped = false
                }
            }
            total += won
            round++
        }
        return total
    }

    fun three(input: List<String>, required: Int = 256): String {
        val spins = input[0].ints()
        val wheels = List(input.size) { mutableListOf<String>() }
        for (line in input.drop(2)) {
            for (i in spins.indices) {
                val faces = if (line.length > i * 4) line.substring(i * 4, i * 4 + 3) else ""
                if (faces.isNotBlank()) wheels[i].add("${faces[0]}${faces[2]}")
            }
        }

        fun coins(indexes: IntArray): Long {
            var total = 0L
            var round = 0L
            while (round < required) {
                for (i in spins.indices) {
                    indexes[i] = (indexes[i] + spins[i]) % wheels[i].size
                }
                val line = indexes.indices.joinToString("") { wheels[it][indexes[it]] }
                val won = line.groupingBy { it }.eachCount().values.sumOf { max(0, it - 2) }
                total += won
                round++
            }
            return total
        }

        // I think the issue is that this "move indices up or down before the pull" must be down before every pull
        // instead of just at the beginning
        val i0 = coins(spins.indices.map { 0 }.toIntArray())
        val i1 = coins(spins.indices.map { 1 }.toIntArray())
        val i2 = coins(spins.indices.map { wheels[it].lastIndex }.toIntArray())
        val l = listOf(i0, i1, i2)
        val min = l.min()
        val max = l.max()
        return "$max $min"
    }
}

val Quest16Test by testSuite {
    val quest = "16"

    with(Quest16) {
        test("one") {
            val sample = """
                1,2,3

                ^_^ -.- ^,-
                >.- ^_^ >.<
                -_- -.- >.<
                    -.^ ^_^
                    >.>
            """.trimIndent().lines()
            one(sample) shouldBe ">.- -.- ^,-"

            val input = lines(quest, 1)
            one(input) shouldBe "*,< *_< *_< ^:^"
        }

        test("two") {
            val sample = """
                1,2,3

                ^_^ -.- ^,-
                >.- ^_^ >.<
                -_- -.- >.<
                    -.^ ^_^
                    >.>
            """.trimIndent().lines()
            two(sample, 10L) shouldBe 15L
            two(sample, 100L) shouldBe 138L
            two(sample, 1000L) shouldBe 1383L
            two(sample) shouldBe 280014668134L

            val input = lines(quest, 2)
            two(input) shouldBe 123865617341L
        }
        test("three") {
            val sample = """
                1,2,3

                ^_^ -.- ^,-
                >.- ^_^ >.<
                -_- -.- ^.^
                    -.^ >.<
                    >.>
            """.trimIndent().lines()
            three(sample, 1) shouldBe "4 1"
            three(sample, 3) shouldBe "9 2"
            three(sample, 256) shouldBe "627 128"

//            val input = lines(quest, 3)
//            three(input) shouldBe 0
        }
    }
}
