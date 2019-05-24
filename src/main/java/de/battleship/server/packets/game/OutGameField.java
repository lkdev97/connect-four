package de.battleship.server.packets.game;

public class OutGameField extends GamePacket {
    public String gameField;

    public OutGameField(String gameFieldString) {
        this.gameField = gameFieldString;
    }
}
