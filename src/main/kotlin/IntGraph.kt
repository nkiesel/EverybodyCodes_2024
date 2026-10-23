class IntGraph {
    val nodes = mutableSetOf<Int>()
    val edges = mutableSetOf<Pair<Int, Int>>()

    fun addEdge(a: Int, b: Int) {
        nodes += a
        nodes += b
        edges += a to b
        edges += b to a
    }

    fun shortest(start: Int, end: Int): List<Int> {
        if (start == end) return listOf(start)
        val queue = ArrayDeque<List<Int>>()
        queue.add(listOf(start))
        val seen = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val n = queue.removeFirst()
            val p = n.last()
            edges.filter { it.first == p }.map { it.second }.filter { seen.add(it) }.forEach {
                val a = n + it
                if (it == end) return a
                queue.add(a)
            }
        }
        return emptyList()
    }
}

fun main() {
    val g = IntGraph()
    g.addEdge(2, 3)
    g.addEdge(0, 1)
    g.addEdge(0, 2)

    println(g.shortest(2, 3))
}
