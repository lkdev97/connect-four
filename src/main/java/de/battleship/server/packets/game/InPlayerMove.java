package de.battleship.server.packets.game;

import de.battleship.Game;
import de.battleship.server.GameHandler;
import de.battleship.server.Lobby;
import de.battleship.server.OnlinePlayer;
import io.javalin.websocket.WsSession;

public class InPlayerMove extends GamePacket {
    public int column;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        if (lobby.hasGame()) {
            Game game = lobby.getGame();
            
            if (game.getWinner() == null && player.equals(game.getCurrentPlayer())) {
                game.makeTurn(this.column);

                if (game.getWinner() != null)
                    lobby.sendPacket(new OutChatMessage(game.getWinner().getName() + " hat gewonnen!", OutChatMessage.Type.SUCCESS));
            }

            lobby.sendGameState();
        }
    }
}
