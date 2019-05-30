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
                player.setName(this.playerName);
                gameHandler.addConnectedPlayer(session, player);

                if (lobby.addPlayer(player)) {
                    gameHandler.sendPacket(session, new OutConnectSuccess(this.playerName));
                    lobby.sendGameFieldUpdate();
                } 
                else
                    player.disconnect("Konnte dem Spiel nicht beitreten. Versuche es mit einem anderen Spielernamen.");
            } else
                player.disconnect("Ungültiger Spielername (mindestens 3 Zeichen benötigt).");
        } else
            player.disconnect("Dieses Spiel ist bereits voll.");
    }
}
