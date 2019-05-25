package de.battleship;

import java.util.ArrayList;

public class Lobby {
    private String lobbyId;
    private boolean isPublic;

    private ArrayList<Player> players;

    private Game game;


    public Lobby(String lobbyId, boolean isPublic) {
        this.lobbyId = lobbyId;
        this.isPublic = isPublic;

        this.players = new ArrayList<Player>();
    }
    

    public boolean addPlayer(Player player) {
        return true;
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }


    public String getLobbyId() {
        return this.lobbyId;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public Game getGame() {
        return this.game;
    }
}
