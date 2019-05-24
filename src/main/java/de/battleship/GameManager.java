package de.battleship;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collection;

/**
 * Verwaltet alle aktiven Spiele.
 */
public class GameManager {
    private static final int MAX_GAMES_AMOUNT = 20;



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

        if (this.activeGames.size() < MAX_GAMES_AMOUNT) {
            Game game = this.activeGames.put(gameId, new Game("Player1", "Player2"));
            System.out.println("Created new game with ID " + gameId + ", total amount now: " + this.activeGames.size());

            if (isPublic) {
                this.publicGames.put(gameId, game);
                App.getWebHandler().broadcastNewPublicGame(gameId);
            }
        }
        else
            gameId = null;

        return gameId;
    }

    /**
     * Gibt ein Spiel mit einer gegebenen ID zurück.
     */
    public Game getGameById(String id) {
        return (id != null) ? this.activeGames.getOrDefault(id.toUpperCase(), null) : null;
    }

    /**
     * Löscht das angegebene Spiel aus der Liste der aktiven Spiele.
     */
    public void removeGame(Game game) {
        this.publicGames.values().remove(game);

        String key = null;
        for (Entry<String, Game> entry : this.activeGames.entrySet()) {
            if (entry.getValue() == game) {
                key = entry.getKey();
                App.getWebHandler().broadcastRemovePublicGame(key);
                System.out.println("Removed a game, total amount now: " + this.activeGames.size());
                break;
            }
        }

        if (key != null)
            this.activeGames.remove(key);
    }


    public Collection<String> getPublicGames() {
        return this.publicGames.keySet();
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
                if (Math.random() > 0.4) // häufiger Buchstaben als Zahlen
                    id.append((char)(int)(Math.random() * 26 + 65)); // A-Z
                else
                    id.append((char)(int)(Math.random() * 9 + 48)); // 0-9
            }
        }
        while (this.activeGames.containsKey(id.toString()));

        return id.toString();
    }
}