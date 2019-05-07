package api;

import api.packets.InJoinGame;
import api.packets.OutError;
import api.packets.OutMessage;
import api.packets.Packet;
import io.javalin.Context;
import io.javalin.Javalin;

/**
 * Bearbeitet Anfragen per Web-API.
 */
public class ApiClientHandler {
    /**
     * Die Serverinstanz, welche dieser Handler abhört.
     */
    private Javalin server;


    public ApiClientHandler(Javalin server) {
        this.server = server;

        this.server.post("/create", this::handleCreateGame);
        this.server.post("/join", this::handleJoinGame);
    }


    /**
     * Wird aufgerufen, wenn der Client ein neues Spiel erstellen möchte.
     */
    private void handleCreateGame(Context ctx) {
        this.sendPacket(ctx, new OutMessage("Game created."));
    }

    /**
     * Wird aufgerufen, wenn der Client einem Spiel per GameID beitreten möchte.
     */
    private void handleJoinGame(Context ctx) {
        try {
            InJoinGame in = ctx.bodyAsClass(InJoinGame.class);
            this.sendPacket(ctx, new OutMessage("Game with ID " + in.gameId + " joined successfully."));
        } catch (Exception ex) {
            this.sendError(ctx, "Invalid request.");
        }
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
