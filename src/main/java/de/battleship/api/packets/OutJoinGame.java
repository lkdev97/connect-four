package de.battleship.api.packets;

public class OutJoinGame extends Packet {
    public boolean success;

    public OutJoinGame(boolean success) {
        this.success = success;
    }
}