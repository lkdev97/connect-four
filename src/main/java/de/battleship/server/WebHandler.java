package de.battleship.server;

import java.util.ArrayList;

import de.battleship.App;
import de.battleship.server.packets.web.InCreateGame;
import de.battleship.server.packets.web.OutCreateGame;
import de.battleship.server.packets.web.OutError;
import de.battleship.server.packets.web.OutPublicGamesList;
import de.battleship.server.packets.web.WebPacket;
import io.javalin.Context;
import io.javalin.Javalin;
import io.javalin.serversentevent.SseClient;

/**
 * Bearbeitet Anfragen, die von der Hauptseite ausgehen.
 */
public class WebHandler {
    /**
     * Die Serverinstanz, welche dieser Handler abhört.
     */
    private Javalin server;

    /**
     * Eine Liste, die alle SseClients speichert, welche die Games-Browser Events abhören.
     */
    private ArrayList<SseClient> gamesListEventClients;

    public WebHandler(Javalin server) {
        this.server = server;
        this.gamesListEventClients = new ArrayList<SseClient>();

        this.server.post("/create", this::handleCreateGame);
        this.server.post("/gamelist", this::handlePublicGamesList);
        this.server.sse("/gamelist", client -> {
            client.onClose(() -> this.gamesListEventClients.remove(client));
            this.gamesListEventClients.add(client);
        });
    }

    public void broadcastNewPublicGame(String gameId) {
        for (int i = this.gamesListEventClients.size() - 1; i >= 0; i--)
            this.gamesListEventClients.get(i).sendEvent("addgame", gameId);
    }

    public void broadcastRemovePublicGame(String gameId) {
        for (int i = this.gamesListEventClients.size() - 1; i >= 0; i--)
            this.gamesListEventClients.get(i).sendEvent("rmgame", gameId);
    }

    /**
     * Wird aufgerufen, wenn der Client ein neues Spiel erstellen möchte.
     */
    private void handleCreateGame(Context ctx) {
        try {
            InCreateGame in = ctx.bodyAsClass(InCreateGame.class);
            this.sendPacket(ctx, new OutCreateGame(App.getGameManager().createNewGame(in.isPublic)));
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
    private void sendPacket(Context ctx, WebPacket packet) {
        ctx.json(packet);
    }

    /**
     * Sendet ein Fehlerpacket an den Client.
     */
    private void sendError(Context ctx, String message) {
        this.sendPacket(ctx, new OutError(message));
    }
}
