package de.battleship.server.packets.web;

public class OutCreateGame extends WebPacket {
    public String gameId;


    public OutCreateGame(String gameId) {
        this.gameId = gameId;
    }
}