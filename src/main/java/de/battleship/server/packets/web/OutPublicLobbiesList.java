package de.battleship.server.packets.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.battleship.Lobby;

public class OutPublicLobbiesList extends WebPacket {
    public List<Lobby.Data> lobbies;


    public OutPublicLobbiesList(Collection<Lobby> publicLobbies) {
        this.lobbies = new ArrayList<>(publicLobbies.size());
        publicLobbies.forEach(lobby -> this.lobbies.add(lobby.getData()));
    }
}