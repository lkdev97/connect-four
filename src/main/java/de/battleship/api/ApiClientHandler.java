package de.battleship.api;

import de.battleship.App;
import de.battleship.Game;
import de.battleship.api.packets.InCreateGame;
import de.battleship.api.packets.InJoinGame;
import de.battleship.api.packets.OutError;
import de.battleship.api.packets.OutMessage;
import de.battleship.api.packets.Packet;
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
        try {
            InCreateGame in = ctx.bodyAsClass(InCreateGame.class);
            String gameId = App.getGameManager().createNewGame(in.isPublic);
            this.sendPacket(ctx, new OutMessage("Game created. Your Game ID: " + gameId));
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
            Game game = App.getGameManager().getGameById(in.gameId);
            String message = (game != null) ? ("Game with ID " + in.gameId + " joined successfully.") : "Game not found.";

            this.sendPacket(ctx, new OutMessage(message));
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
