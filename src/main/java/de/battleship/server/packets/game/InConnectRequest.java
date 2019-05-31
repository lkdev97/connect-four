package de.battleship.server.packets.game;

import de.battleship.server.GameHandler;
import de.battleship.server.Lobby;
import de.battleship.server.OnlinePlayer;
import io.javalin.websocket.WsSession;

public class InConnectRequest extends GamePacket {
    public String playerName;

    @Override
    public void handle(GameHandler gameHandler, WsSession session, Lobby lobby, OnlinePlayer player) {
        if (lobby.areSpectatorsAllowed() || lobby.getPlayersAmount() < lobby.getMaxPlayersAmount()) {
            this.playerName = this.playerName.replace(" ", "");

            if (this.playerName.length() >= 3) {
                player.setName(this.playerName);

                if (lobby.addPlayer(player)) {
                    player.sendPacket(new OutConnectSuccess(lobby.getLobbyId(), this.playerName));

                    // versuche, das Spiel zu starten, falls die Lobby noch kein laufendes Spiel hat
                    if (!lobby.hasGame())
                        lobby.startGame();
                } 
                else
                    player.disconnect("Konnte dem Spiel nicht beitreten. Versuche es mit einem anderen Spielernamen.");
            } else
                player.disconnect("Ungültiger Spielername (mindestens 3 Zeichen benötigt).");
        } else
            player.disconnect("Dieses Spiel ist bereits voll.");
    }
}
