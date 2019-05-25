package de.battleship.server.packets.game;

import de.battleship.Game;
import de.battleship.Player;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InPlayerMove extends GamePacket {
    public int column;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Player player, Game game) {
        if (game.getCurrentPlayer().equals(player))
            game.makeTurn(this.column);
        
        gameHandler.sendPacket(session, new OutGameField(game.toString()));
    }
}
