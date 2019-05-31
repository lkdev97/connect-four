package de.battleship.server.packets.game;

import de.battleship.server.GameHandler;
import de.battleship.server.Lobby;
import de.battleship.server.OnlinePlayer;
import io.javalin.websocket.WsSession;

public class InPingPacket extends GamePacket {
    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        
    }
}
