package de.battleship.server.packets.web;

public class OutCreateLobby extends WebPacket {
    public String lobbyId;


    public OutCreateLobby(String lobbyId) {
        this.lobbyId = lobbyId;
    }
}