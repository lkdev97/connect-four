package de.battleship.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.battleship.App;
import de.battleship.Game;
import de.battleship.Player;
import de.battleship.server.packets.game.GamePacket;
import de.battleship.server.packets.game.OutChatMessage;
import de.battleship.server.packets.game.OutGameState;

public class Lobby {
    private String lobbyId;
    private boolean isPublic;

    private ArrayList<Player> players;
    private int maxPlayers;
    private ArrayList<Player> spectators;
    private boolean spectatorsAllowed;

    private Game game;


    public Lobby(String lobbyId, boolean isPublic) {
        this.lobbyId = lobbyId;
        this.isPublic = isPublic;

        this.players = new ArrayList<Player>();
        this.maxPlayers = 2;
        this.spectators = new ArrayList<Player>();
        this.spectatorsAllowed = true;
    }
    

    public boolean addPlayer(Player player) {
        if (canJoin(player)) {
            if (this.players.size() < this.maxPlayers)
                this.players.add(player);
            else
                this.spectators.add(player);

            this.checkGamePlayers();

            if (this.isPublic())
                App.getWebHandler().broadcastUpdatePublicLobby(this);

            this.sendGameState();
            this.sendPacket(new OutChatMessage(">> " + player.getName() + " ist dem Spiel beigetreten", OutChatMessage.Type.SUCCESS));
            return true;
        }

        return false;
    }

    public void removePlayer(Player player) {
        if (this.hasGame()) {
            if (player.equals(this.game.getP1()))
                this.game.setP1(null);
            if (player.equals(this.game.getP2()))
                this.game.setP2(null);
        }

        if (this.players.remove(player) || this.spectators.remove(player))
            this.sendPacket(new OutChatMessage("<< " + player.getName() + " hat das Spiel verlassen",
                    OutChatMessage.Type.WARNING));
        
        this.checkGamePlayers();

        if (this.isPublic())
            App.getWebHandler().broadcastUpdatePublicLobby(this);

        if (this.players.size() <= 0)
            App.getLobbyManager().removeLobby(this);

        this.sendGameState();
    }
    

    public void startGame() {
        if (!this.hasGame() && this.getPlayersAmount() >= 2) {
            this.game = new Game(this.players.get(0), this.players.get(1));
            this.sendGameState();
        }
    }


    public void sendPacket(GamePacket packet) {
        // Spieler
        for (int i = this.players.size() - 1; i >= 0; i--)
            if (this.players.get(i) instanceof OnlinePlayer)
                ((OnlinePlayer) this.players.get(i)).sendPacket(packet);

        // Zuschauer
        for (int i = this.spectators.size() - 1; i >= 0; i--)
            if (this.spectators.get(i) instanceof OnlinePlayer)
                ((OnlinePlayer) this.spectators.get(i)).sendPacket(packet);
    }
    public void sendGameState() {
        if (this.hasGame()) {
            String status = "";
            if (this.game.getWinner() != null)
                status = this.game.getWinner().getName() + " hat gewonnen!";
            else if (this.game.getCurrentPlayer() != null)
                status = this.game.getCurrentPlayer().getName() + " ist am Zug";
            else
                status = "Warte auf mehr Spieler...";

            this.sendPacket(new OutGameState(status, this.game.toString()));
        }
    }
    
    public boolean canJoin(Player player) {
        if (!this.areSpectatorsAllowed() && this.players.size() >= this.maxPlayers)
            return false;
        
        for (int i = 0; i < this.players.size(); i++)
            if (this.players.get(i).getName().equalsIgnoreCase(player.getName()))
                return false;
        for (int i = 0; i < this.spectators.size(); i++)
            if (this.spectators.get(i).getName().equalsIgnoreCase(player.getName()))
                return false;

        return true;
    }

    
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }
    public List<Player> getSpectators() {
        return Collections.unmodifiableList(this.spectators);
    }

    public String getLobbyId() {
        return this.lobbyId;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean areSpectatorsAllowed() {
        return this.spectatorsAllowed;
    }

    public int getPlayersAmount() {
        return this.players.size();
    }
    public int getMaxPlayersAmount() {
        return this.maxPlayers;
    }
    public int getSpectatorsAmount() {
        return this.spectators.size();
    }

    public Lobby.Data getData() {
        return new Lobby.Data(this.lobbyId, this.players.size(), this.maxPlayers, this.spectators.size());
    }

    public Game getGame() {
        return this.game;
    }

    public boolean hasGame() {
        return this.game != null;
    }


    private void checkGamePlayers() {
        if (this.hasGame()) {
            if (this.game.getP1() == null)
                this.game.setP1(this.findNewPlayer(this.game.getP2()));
            if (this.game.getP2() == null)
                this.game.setP2(this.findNewPlayer(this.game.getP1()));
        }
    }

    private Player findNewPlayer(Player exceptPlayer) {
        // zuerst in der aktiven Spielerliste suchen (z. B. reconnect?)
        for (int i = 0; i < this.players.size(); i++)
            if (!this.players.get(i).equals(exceptPlayer))
                return this.players.get(i);
        
        // falls kein Spieler gefunden wurde, dann wird ein Zuschauer als Spieler eingetragen
        if (this.spectators.size() > 0) {
            Player player = this.spectators.get(0);
            this.players.add(player);
            this.spectators.remove(0);

            return player;
        }

        return null;
    }





    public static class Data {
        public String lobbyId;
        public int players;
        public int maxPlayers;
        public int spectators;

        public Data(String lobbyID, int players, int maxPlayers, int spectators) {
            this.lobbyId = lobbyID;
            this.players = players;
            this.maxPlayers = maxPlayers;
            this.spectators = spectators;
        }
    }
}
