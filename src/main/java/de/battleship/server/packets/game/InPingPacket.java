package de.battleship.server.packets.game;

import de.battleship.Lobby;
import de.battleship.OnlinePlayer;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InPingPacket extends GamePacket {
    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        
    }
}
