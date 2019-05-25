package de.battleship.server.packets.game;

import de.battleship.Game;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InConnectRequest extends GamePacket {
    public String playerName;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Game game) {
        gameHandler.sendPacket(session, new OutGameField(game.toString()));
    }
}