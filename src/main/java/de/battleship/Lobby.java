package de.battleship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lobby {
    private String lobbyId;
    private boolean isPublic;

    private ArrayList<Player> players;
    private int maxPlayers;

    private Game game;


    public Lobby(String lobbyId, boolean isPublic) {
        this.lobbyId = lobbyId;
        this.isPublic = isPublic;

        this.players = new ArrayList<Player>();
        this.maxPlayers = 2;
    }
    

    public boolean addPlayer(Player player) {
        if (this.players.size() < this.maxPlayers) {
            this.players.add(player);
            return true;
        }

        return false;
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
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
