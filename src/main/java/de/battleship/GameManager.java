package de.battleship;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Verwaltet alle aktiven Spiele.
 */
public class GameManager {
    /**
     * Speichert alle aktiven Spiele mit ihrer ID als Key.
     */
    private HashMap<String, Game> activeGames;
    /**
     * Speichert alle öffentlichen Spiele mit ihrer ID als Key.
     */
    private HashMap<String, Game> publicGames;


    /**
     * Erstellt ein neues GameManager Objekt.
     */
    public GameManager() {
        this.activeGames = new HashMap<>();
        this.publicGames = new HashMap<>();
    }


    /**
     * Erstellt ein neues Spiel und gibt die Spiel-ID zurück.
     */
    public String createNewGame(boolean isPublic) {
        String gameId = this.generateNewId();
        Game game = this.activeGames.put(gameId, new Game("Player1", "Player2"));
        System.out.println("Created new game with ID " + gameId + ", total amount now: " + this.activeGames.size());

        if (isPublic)
            this.publicGames.put(gameId, game);

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
        this.publicGames.values().remove(game);
        System.out.println("Removed a game, total amount now: " + this.activeGames.size());
    }


    public Collection<String> getPublicGames() {
        return this.activeGames.keySet();
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