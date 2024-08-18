// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.model

enum class GameType(val expected: Boolean) {
    REGULAR(true),
    MISERE(false)
}
