package de.battleship.server.packets.game;

public class OutConnectSuccess extends GamePacket {
    public String playerName;

    public OutConnectSuccess(String sanitizedPlayerName) {
        this.playerName = sanitizedPlayerName;
    }
}
