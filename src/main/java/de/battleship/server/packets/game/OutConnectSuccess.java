package de.battleship.server.packets.game;

public class OutConnectSuccess extends GamePacket {
    public String lobbyId;
    public String playerName;

    public OutConnectSuccess(String sanitizedLobbyId, String sanitizedPlayerName) {
        this.lobbyId = sanitizedLobbyId;
        this.playerName = sanitizedPlayerName;
    }
}
