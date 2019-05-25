package de.battleship;

import java.util.HashMap;
import java.util.Collection;

/**
 * Verwaltet alle aktiven Lobbies.
 */
public class LobbyManager {
    private static final int MAX_LOBBIES_AMOUNT = 20;



    /**
     * Speichert alle aktiven Lobbies mit ihrer ID als Key.
     */
    private HashMap<String, Lobby> activeLobbies;
    /**
     * Speichert alle öffentlichen Lobbies mit ihrer ID als Key.
     */
    private HashMap<String, Lobby> publicLobbies;


    /**
     * Erstellt ein neues LobbyManager Objekt.
     */
    public LobbyManager() {
        this.activeLobbies = new HashMap<>();
        this.publicLobbies = new HashMap<>();
    }


    /**
     * Erstellt eine neue Lobby und gibt die Lobby-ID zurück.
     */
    public String createNewLobby(boolean isPublic) {
        String lobbyId = this.generateNewId();

        if (this.activeLobbies.size() < MAX_LOBBIES_AMOUNT) {
            Lobby lobby = this.activeLobbies.put(lobbyId, new Lobby(lobbyId, isPublic));
            System.out.println("Created new lobby with ID " + lobbyId + ", total amount now: " + this.activeLobbies.size());

            if (isPublic) {
                this.publicLobbies.put(lobbyId, lobby);
                App.getWebHandler().broadcastNewPublicLobby(lobbyId);
            }
        }
        else
            lobbyId = null;

        return lobbyId;
    }

    /**
     * Gibt eine Lobby mit einer angegebenen ID zurück.
     */
    public Lobby getLobbyById(String id) {
        return (id != null) ? this.activeLobbies.getOrDefault(id.toUpperCase(), null) : null;
    }

    /**
     * Löscht die angegebene Lobby aus der Liste der aktiven Lobbies.
     */
    public void removeLobby(Lobby lobby) {
        String lobbyId = lobby.getLobbyId();

        if (this.activeLobbies.containsKey(lobbyId)) {
            this.activeLobbies.remove(lobbyId);

            if (lobby.isPublic()) {
                this.publicLobbies.remove(lobbyId);
                App.getWebHandler().broadcastRemovePublicLobby(lobbyId);
            }

            System.out.println("Removed a lobby, total amount now: " + this.activeLobbies.size());
        }
    }


    public Collection<Lobby> getPublicLobbies() {
        return this.publicLobbies.values();
    }


    /**
     * Generiert eine neue Lobby-ID.
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
        while (this.activeLobbies.containsKey(id.toString()));

        return id.toString();
    }
}