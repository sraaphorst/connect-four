package org.vorpal.connect.general

// Transpose an n x m matrix into an m x n matrix.
fun <A> List<List<A>>.transpose(): List<List<A>> {
    val rowCount = size
    val colCount = first().size
    if (all { it.size != colCount })
        throw IllegalArgumentException("Non-matrix cannot be transposed.")

    return List(colCount) { col ->
        List(rowCount) { row ->
            this[row][col]
        }
    }
}

typealias Position = Pair<Int, Int>

// Transpose a vector of (x, y) positions into a vector of (y, x) positions.
fun List<Position>.transpose(): List<Position> =
    map { it.second to it.first}

