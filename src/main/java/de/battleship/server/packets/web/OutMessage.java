package de.battleship.server.packets.web;

public class OutMessage extends WebPacket {
    public String message;

    public OutMessage(String message) {
        this.message = message;
    }
}