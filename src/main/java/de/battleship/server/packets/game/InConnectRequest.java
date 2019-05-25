package de.battleship.server.packets.game;

import de.battleship.Game;
import de.battleship.Lobby;
import de.battleship.Player;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InConnectRequest extends GamePacket {
    public String playerName;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, Player player) {
        if (lobby.getGame() != null)
            gameHandler.sendPacket(session, new OutGameField(lobby.getGame().toString()));
    }
}