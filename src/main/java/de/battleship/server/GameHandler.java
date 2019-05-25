package de.battleship.server;

import de.battleship.App;
import de.battleship.Game;
import de.battleship.Lobby;
import de.battleship.server.packets.game.GamePacket;
import de.battleship.server.packets.game.OutError;
import de.battleship.server.packets.game.OutGameField;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;

public class GameHandler {
    private Javalin server;


    public GameHandler(Javalin server) {
        this.server = server;

        this.server.ws("/:lobby-id", ws -> {
            ws.onConnect(this::handleNewClient);
            ws.onMessage(this::handleClientMessage);
            ws.onClose(this::handleClientDisconnect);
        });
    }


    /**
     * Sendet ein Packet an den Client.
     */
    public void sendPacket(WsSession session, GamePacket packet) {
        session.send(packet.toString());
    }
    
    /**
     * Sendet eine Fehlernachricht an den Client.
     * Trennt nicht die Verbindung.
     */
    public void sendErrorMessage(WsSession session, String message) {
        this.sendErrorMessage(session, message, false);
    }
    /**
     * Sendet eine Fehlernachricht an den Client.
     * Trennt danach die Verbindung, falls disconnect auf true gesetzt wurde.
     */
    public void sendErrorMessage(WsSession session, String message, boolean disconnect) {
        this.sendPacket(session, new OutError(message));

        if (disconnect)
            session.close(1, "Disconnect by server (error).");
    }


    /**
     * Wird aufgerufen, wenn sich ein neuer Client mit dem Server verbindet.
     */
    private void handleNewClient(WsSession session) {
        Lobby lobby = App.getLobbyManager().getLobbyById(session.pathParam("lobby-id"));

        if (lobby == null)
            this.sendErrorMessage(session, "Lobby not found.", true);
    }
    /**
     * Wird aufgerufen, wenn ein Client eine Nachricht an den Server sendet.
     */
    private void handleClientMessage(WsSession session, String message) {
        Lobby lobby = App.getLobbyManager().getLobbyById(session.pathParam("lobby-id"));
        
        try {
            GamePacket.fromString(message).handle(this, session, lobby.getGame().getCurrentPlayer(), lobby.getGame());
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
                "WebSocket closed for id " + session.pathParam("lobby-id") + ": (" + statusCode + ") " + reason);
    }
}
