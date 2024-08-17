// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.model

enum class Player {
    PLAYER1,
    PLAYER2;

    fun toggle(): Player = when (this) {
        PLAYER1 -> PLAYER2
        PLAYER2 -> PLAYER1
    }
}