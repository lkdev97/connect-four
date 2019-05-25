package de.battleship.server;

import de.battleship.App;
import de.battleship.Game;
import de.battleship.server.packets.game.GamePacket;
import de.battleship.server.packets.game.OutError;
import de.battleship.server.packets.game.OutGameField;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;

public class GameHandler {
    private Javalin server;


    public GameHandler(Javalin server) {
        this.server = server;

        this.server.ws("/:game-id", ws -> {
            ws.onConnect(this::handleNewClient);
            ws.onMessage(this::handleClientMessage);
            ws.onClose(this::handleClientDisconnect);
        });
    }


    /**
     * Wird aufgerufen, wenn sich ein neuer Client mit dem Server verbindet.
     */
    private void handleNewClient(WsSession session) {
        Game game = App.getGameManager().getGameById(session.pathParam("game-id"));

        if (game == null)
            this.sendErrorMessage(session, "Game not found.", true);
    }
    /**
     * Wird aufgerufen, wenn ein Client eine Nachricht an den Server sendet.
     */
    private void handleClientMessage(WsSession session, String message) {
        Game game = App.getGameManager().getGameById(session.pathParam("game-id"));
        
        try {
            GamePacket packet = GamePacket.fromString(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.sendErrorMessage(session, "Invalid packet received.", true);
        }
    }

    /**
     * Wird aufgerufen, wenn ein Client die Verbindung trennt.
     */
    private void handleClientDisconnect(WsSession session, int statusCode, String reason) {
        // TODO: Remove player from lobby
        System.out.println(
                "WebSocket closed for id " + session.pathParam("game-id") + ": (" + statusCode + ") " + reason);
    }
    

    /**
     * Sendet ein Packet an den Client.
     */
    private void sendPacket(WsSession session, GamePacket packet) {
        session.send(packet.toString());
    }
    
    /**
     * Sendet eine Fehlernachricht an den Client.
     * Trennt nicht die Verbindung.
     */
    private void sendErrorMessage(WsSession session, String message) {
        this.sendErrorMessage(session, message, false);
    }
    /**
     * Sendet eine Fehlernachricht an den Client.
     * Trennt danach die Verbindung, falls disconnect auf true gesetzt wurde.
     */
    private void sendErrorMessage(WsSession session, String message, boolean disconnect) {
        this.sendPacket(session, new OutError(message));

        if (disconnect)
            session.close(1, "Disconnect by server (error).");
    }
}
