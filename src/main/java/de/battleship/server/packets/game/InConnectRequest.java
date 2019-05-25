package de.battleship.server.packets.game;

import de.battleship.Lobby;
import de.battleship.Player;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InConnectRequest extends GamePacket {
    public String playerName;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, Player player) {
        if (lobby.getGame() != null) {
            if (lobby.getPlayersAmount() < lobby.getMaxPlayersAmount()) {
                player = new Player(this.playerName);
                gameHandler.addConnectedPlayer(session, player);

                if (lobby.addPlayer(player))
                    gameHandler.sendPacket(session, new OutGameField(lobby.getGame().toString()));
                else
                    gameHandler.sendErrorMessage(session, "Couldn't add you to lobby. Try changing your name.", true);
            }
            else
                gameHandler.sendErrorMessage(session, "Lobby is already full.", true);

        }
    }
}