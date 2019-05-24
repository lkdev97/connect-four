package de.battleship.server.packets.game;

public class OutErrorMessage extends GamePacket {
    public String error;

    public OutErrorMessage(String error) {
        this.error = error;
    }
}
