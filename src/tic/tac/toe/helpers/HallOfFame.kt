package tic.tac.toe.helpers

import java.io.*
import kotlin.collections.ArrayList
import kotlin.math.min


class HallOfFame {
    companion object {
        private const val numberOfSpots = 10

        fun add(name: String, time: Double) {
            val hallOfFame = loadData()!!

            if ( hallOfFame.size < numberOfSpots) {
                if (hallOfFame[0].size == 0) {
                    hallOfFame[0].add(name)
                    hallOfFame[1].add(time.toString())
                } else {
                    var added = false
                    for (i in (0 until hallOfFame[1].size)) {
                        if (hallOfFame[1][i].toDouble() > time) {
                            hallOfFame[0].add(i, name)
                            hallOfFame[1].add(i, time.toString())
                            added = true
                            break
                        }
                    }

                    if (!added) {
                        hallOfFame[0].add(name)
                        hallOfFame[1].add(time.toString())
                    }
                }
            } else {
                var index = -1
                for (i in (0 until hallOfFame[1].size)) {
                    if (hallOfFame[1][i].toDouble() > time) {
                        index = i
                        break
                    }
                }

                if (index > -1) {
                    hallOfFame[0][index] = name
                    hallOfFame[1][index] = time.toString()
                }
            }


            val fw = FileWriter("halloffame.txt")
            val writer = BufferedWriter(fw)

            for (j in (0 until min(hallOfFame[0].size, numberOfSpots))) {
                writer.write("${hallOfFame[0][j]},${hallOfFame[1][j]}\n")
            }

            writer.close()
            fw.close()
        }

        fun belongsToHallOfFame(time: Double): Boolean {
            val hallOfFame = loadData()!!
            if (hallOfFame[0].size < numberOfSpots) {
                return true
            }

            for (j in (0 until hallOfFame[1].size)) {
                if (hallOfFame[1][j].toDouble() > time) {
                    return true
                }
            }
            return false
        }

        fun getHallOfFameString(): String {
            val hallOfFame = loadData()!!
            var result = ""

            for (j in (0 until hallOfFame[0].size)) {
                result += "${j+1}. '${hallOfFame[0][j]}' won in ${hallOfFame[1][j]}s\n"
            }

            return result
        }

        /**
            result[0] -> names
            result[1] -> scores
         */
        private fun loadData(): ArrayList<ArrayList<String>>? {
            try {
                val result = ArrayList<ArrayList<String>>(2)
                val names = ArrayList<String>()
                val scores = ArrayList<String>()
                result.add(names)
                result.add(scores)


                val fr = FileReader("halloffame.txt")
                val reader = BufferedReader(fr)

                var name: String
                var score: String
                var readsName: Boolean // To split up data at the comma
                reader.lines().forEach {
                    readsName = true
                    name = ""
                    score = ""

                    for (char: Char in it) {
                        if (char == ',') {
                            readsName = false
                            continue
                        }

                        if (readsName)
                            name += char
                        else
                            score += char
                    }

                    result[0].add(name)
                    result[1].add(score)
                }

                return result
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }
    }
}