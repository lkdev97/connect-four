package de.battleship.server.packets.game;

import de.battleship.Lobby;
import de.battleship.OnlinePlayer;
import de.battleship.server.GameHandler;
import io.javalin.websocket.WsSession;

public class InConnectRequest extends GamePacket {
    public String playerName;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        if (lobby.getPlayersAmount() < lobby.getMaxPlayersAmount()) {
            this.playerName = this.playerName.replace(" ", "");

            if (this.playerName.length() >= 3) {
                player = new OnlinePlayer(this.playerName, session);
                gameHandler.addConnectedPlayer(session, player);

                if (lobby.addPlayer(player))
                    gameHandler.sendPacket(session, new OutConnectSuccess(this.playerName));
                else
                    gameHandler.sendErrorMessage(session, "Couldn't add you to lobby. Try changing your name.", true);
            } else
                gameHandler.sendErrorMessage(session, "Invalid player name (min. 3 characters).", true);
        } else
            gameHandler.sendErrorMessage(session, "Lobby is already full.", true);
    }
}
