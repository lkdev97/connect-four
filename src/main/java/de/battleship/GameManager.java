package de.battleship;

import java.util.HashMap;

/**
 * Verwaltet alle aktiven Spiele.
 */
public class GameManager {
    /**
     * Speichert alle aktiven Spiele mit ihrer ID als Key.
     */
    private HashMap<String, Game> activeGames;


    /**
     * Erstellt ein neues GameManager Objekt.
     */
    public GameManager() {
        this.activeGames = new HashMap<>();
    }


    /**
     * Erstellt ein neues Spiel und gibt die Spiel-ID zurück.
     */
    public String createNewGame() {
        String gameId = this.generateNewId();
        this.activeGames.put(gameId, new Game("Player1", "Player2"));
        System.out.println("Created new game with ID " + gameId + ", total amount now: " + this.activeGames.size());

        return gameId;
    }

    /**
     * Gibt ein Spiel mit einer gegebenen ID zurück.
     */
    public Game getGameById(String id) {
        return this.activeGames.getOrDefault(id, null);
    }

    /**
     * Löscht das angegebene Spiel aus der Liste der aktiven Spiele.
     */
    public void removeGame(Game game) {
        this.activeGames.values().remove(game);
        System.out.println("Removed a game, total amount now: " + this.activeGames.size());
    }


    /**
     * Generiert eine neue Spiel-ID.
     */
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