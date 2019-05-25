package de.battleship.server.packets.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.battleship.Lobby;

public class OutPublicLobbiesList extends WebPacket {
    public List<LobbyData> lobbies;


    public OutPublicLobbiesList(Collection<Lobby> publicLobbies) {
        this.lobbies = new ArrayList<>(publicLobbies.size());
        publicLobbies.stream().forEach(lobby -> this.lobbies
                .add(new LobbyData(lobby.getLobbyId(), lobby.getPlayers().size(), lobby.getMaxPlayersAmount())));
    }




    private class LobbyData {
        @SuppressWarnings("unused")
        public String lobbyId;
        @SuppressWarnings("unused")
        public int players;
        @SuppressWarnings("unused")
        public int maxPlayers;

        public LobbyData(String lobbyID, int players, int maxPlayers) {
            this.lobbyId = lobbyID;
            this.players = players;
            this.maxPlayers = maxPlayers;
        }
    }
}