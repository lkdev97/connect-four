package de.battleship;

import java.util.HashMap;

public class GameManager {
    private HashMap<String, Game> activeGames;


    public GameManager() {
        this.activeGames = new HashMap<>();
    }


    public String createNewGame() {
        String gameId = this.generateNewId();
        this.activeGames.put(gameId, new Game("User1", "User2"));

        return gameId;
    }

    public Game getGameById(String id) {
        return this.activeGames.getOrDefault(id, null);
    }



    private String generateNewId() {
        StringBuilder id = new StringBuilder();

        do
        {
            id.setLength(0);
            for (int i = 0; i < 6; i++) {
                if (Math.random() > 0.5)
                    id.append((char)(int)(Math.random() * 26 + 65)); // A-Z
                else
                    id.append((char)(int)(Math.random() * 9 + 48)); // 0-9
            }
        }
        while (this.activeGames.containsKey(id.toString()));

        return id.toString();
    }
}