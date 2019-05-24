package de.battleship.server.packets.web;

public class OutError extends WebPacket {
    public String error;

    public OutError(String error) {
        this.error = error;
    }
}