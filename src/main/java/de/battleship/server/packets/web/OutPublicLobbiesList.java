package de.battleship.server.packets.web;

import java.util.Collection;

public class OutPublicLobbiesList extends WebPacket {
    public Collection<String> lobbies;


    public OutPublicLobbiesList(Collection<String> publicLobbies) {
        this.lobbies = publicLobbies;
    }
}