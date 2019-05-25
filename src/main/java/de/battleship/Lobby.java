package de.battleship;

import java.util.ArrayList;

import io.javalin.Javalin;

public class Lobby {
    private String lobbyId;

    private ArrayList<Player> players;


    public Lobby(String lobbyId, Javalin server) {
        this.lobbyId = lobbyId;
        this.players = new ArrayList<Player>();

    }
    

    public boolean addPlayer(Player player) {
        return true;
    }
    public void removePlayer(Player player) {
        this.players.remove(player);
    }
}
