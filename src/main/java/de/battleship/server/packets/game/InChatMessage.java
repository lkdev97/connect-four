package de.battleship.server.packets.game;

import de.battleship.server.GameHandler;
import de.battleship.server.Lobby;
import de.battleship.server.OnlinePlayer;
import de.battleship.server.packets.game.OutChatMessage;
import io.javalin.websocket.WsSession;

public class InChatMessage extends GamePacket {
    public String content;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        if (this.content.length() <= 128) {
            if (this.content.replace(" ", "").length() > 0)
                lobby.sendPacket(new OutChatMessage(player.getName(), this.content.trim().replaceAll("\\s+", " "), OutChatMessage.Type.NORMAL));
        } else
            player.sendErrorMessage("Nachricht zu lang! Eine Nachricht darf maximal 128 Zeichen lang sein.");
    }
}
