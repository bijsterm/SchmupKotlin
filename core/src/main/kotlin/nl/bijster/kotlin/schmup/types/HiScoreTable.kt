package nl.bijster.kotlin.schmup.types

class HiScoreTable() {

    companion object HiScores {
        val scoreComparator = compareByDescending<Pair<Int, Double>> { it.first }
        val ScoreAndLevelComparator = scoreComparator.thenByDescending { it.second }
        private var hiScores: MutableList<Pair<Int, Double>> = ArrayList()

        fun get(key: Int): Pair<Int, Double> {
            return hiScores[key]
        }

        fun get(): List<Pair<Int, Double>> {
            return hiScores
        }

        fun add(key: Int, value: Double) {
            hiScores.add(Pair(key, value))
            hiScores = hiScores.sortedWith(ScoreAndLevelComparator).take(10).toMutableList()
        }

    }
}
