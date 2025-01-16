package de.battleship.server.packets.game;

public class OutError extends GamePacket {
    public String error;

    public OutError(String error) {
        this.error = error;
    }
}
