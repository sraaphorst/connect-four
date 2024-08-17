// By Sebastian Raaphorst, 2024.

package org.vorpal.connect.view

import javafx.scene.paint.Color
import org.vorpal.connect.model.Player

object PlayerColorMapper {
    fun getColorForPlayer(player: Player?) = when (player) {
        Player.PLAYER1 -> Color.YELLOW
        Player.PLAYER2 -> Color.RED
        null -> Color.BLACK
    }
}
