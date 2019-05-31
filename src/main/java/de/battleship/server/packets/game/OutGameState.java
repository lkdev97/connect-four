package de.battleship.server.packets.game;

public class OutGameState extends GamePacket {
    public String status;
    public String gameField;

    public OutGameState(String status, String gameFieldString) {
        this.status = status;
        this.gameField = gameFieldString;
    }
}
