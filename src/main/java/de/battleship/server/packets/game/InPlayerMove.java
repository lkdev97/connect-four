package de.battleship.server.packets.game;

import de.battleship.Game;
import de.battleship.Lobby;
import de.battleship.OnlinePlayer;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InPlayerMove extends GamePacket {
    public int column;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        if (lobby.hasGame()) {
            Game game = lobby.getGame();
            
            if (game.getWinner().length() == 0 && game.getCurrentPlayer().equals(player)) {
                game.makeTurn(this.column);

                if (game.getWinner().length() > 0)
                    lobby.sendPacket(new OutChatMessage(game.getWinner() + " hat gewonnen!", OutChatMessage.Type.SUCCESS));
            }

            lobby.sendGameFieldUpdate();
        }
    }
}
