package de.battleship.server.packets.web;

public class OutJoinGame extends WebPacket {
    public boolean success;

    public OutJoinGame(boolean success) {
        this.success = success;
    }
}