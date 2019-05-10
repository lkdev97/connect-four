package de.battleship.api;

import java.util.ArrayList;

import de.battleship.App;
import de.battleship.Game;
import de.battleship.api.packets.InCreateGame;
import de.battleship.api.packets.InJoinGame;
import de.battleship.api.packets.OutError;
import de.battleship.api.packets.OutMessage;
import de.battleship.api.packets.OutPublicGamesList;
import de.battleship.api.packets.Packet;
import io.javalin.Context;
import io.javalin.Javalin;
import io.javalin.serversentevent.SseClient;

/**
 * Bearbeitet Anfragen per Web-API.
 */
public class WebApiHandler {
    /**
     * Die Serverinstanz, welche dieser Handler abhört.
     */
    private Javalin server;

    /**
     * Eine Liste, die alle SseClients speichert, welche die Games-Browser Events abhören.
     */
    private ArrayList<SseClient> gamesListEventClients;


    public WebApiHandler(Javalin server) {
        this.server = server;
        this.gamesListEventClients = new ArrayList<SseClient>();

        this.server.post("/create", this::handleCreateGame);
        this.server.post("/join", this::handleJoinGame);
        this.server.post("/gamelist", this::handlePublicGamesList);
        this.server.sse("/gamelist", client -> {
            client.onClose(() -> this.gamesListEventClients.remove(client));
            this.gamesListEventClients.add(client);
        });
    }


    public void broadcastNewPublicGame(String gameId) {
        for (int i = this.gamesListEventClients.size() -1; i >= 0; i--)
            this.gamesListEventClients.get(i).sendEvent("addgame", gameId);
    }
    public void broadcastRemovePublicGame(String gameId) {
        for (int i = this.gamesListEventClients.size() -1; i >= 0; i--)
            this.gamesListEventClients.get(i).sendEvent("rmgame", gameId);
    }



    /**
     * Wird aufgerufen, wenn der Client ein neues Spiel erstellen möchte.
     */
    private void handleCreateGame(Context ctx) {
        try {
            InCreateGame in = ctx.bodyAsClass(InCreateGame.class);
            String gameId = App.getGameManager().createNewGame(in.isPublic);
            this.sendPacket(ctx, new OutMessage((in.isPublic ? "Public" : "Private") + " game created. Your Game ID: " + gameId));
        } catch (Exception ex) {
            this.sendError(ctx, "Invalid request.");
        }
    }

    /**
     * Wird aufgerufen, wenn der Client einem Spiel per GameID beitreten möchte.
     */
    private void handleJoinGame(Context ctx) {
        try {
            InJoinGame in = ctx.bodyAsClass(InJoinGame.class);
            String gameId = in.gameId.toUpperCase().replace(" ", "");

            Game game = App.getGameManager().getGameById(gameId);
            String message = (game != null) ? ("Game with ID " + in.gameId + " joined successfully.")
                    : "Game not found.";

            this.sendPacket(ctx, new OutMessage(message));
        } catch (Exception ex) {
            this.sendError(ctx, "Invalid request.");
        }
    }

    private void handlePublicGamesList(Context ctx) {
        this.sendPacket(ctx, new OutPublicGamesList(App.getGameManager().getPublicGames()));
    }
    
    
    /**
     * Sendet ein Packet an den Client.
     */
    private void sendPacket(Context ctx, Packet packet) {
        ctx.json(packet);
    }
    /**
     * Sendet ein Fehlerpacket an den Client.
     */
    private void sendError(Context ctx, String message) {
        this.sendPacket(ctx, new OutError(message));
    }
}
