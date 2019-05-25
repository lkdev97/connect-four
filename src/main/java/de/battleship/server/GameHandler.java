package de.battleship.server;

import java.util.HashMap;

import de.battleship.App;
import de.battleship.Lobby;
import de.battleship.Player;
import de.battleship.server.packets.game.GamePacket;
import de.battleship.server.packets.game.OutError;
import io.javalin.Javalin;
import io.javalin.websocket.WsSession;

/**
 * Ermöglicht eine Kommunikation zwischen den Spielern und dem Server über WebSockets.
 */
public class GameHandler {
    /**
     * Die Serverinstanz, welche dieser Handler abhört.
     */
    private Javalin server;

    /**
     * Eine Map mit allen aktuell verbundenen Spielern.
     * Hat die WebSocket Session des Spielers als Key.
     * Wird benutzt, um bei einem eingehenden Packet schnell den Spieler anhand der Session zu identifizieren.
     */
    private HashMap<WsSession, Player> connectedPlayers;


    public GameHandler(Javalin server) {
        this.server = server;

        this.server.ws("/:lobby-id", ws -> {
            ws.onConnect(this::handleNewClient);
            ws.onMessage(this::handleClientMessage);
            ws.onClose(this::handleClientDisconnect);
        });

        this.connectedPlayers = new HashMap<>();
    }


    public void addConnectedPlayer(WsSession session, Player player) {
        this.connectedPlayers.put(session, player);
    }
    public void removeConnectedPlayer(WsSession session) {
        this.connectedPlayers.remove(session);
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
            GamePacket.fromString(message).handle(this, session, lobby, this.connectedPlayers.getOrDefault(session, null));
        } catch (Exception ex) {
            ex.printStackTrace();
            this.sendErrorMessage(session, "Invalid packet received.", true);
        }
    }

    /**
     * Wird aufgerufen, wenn ein Client die Verbindung trennt.
     */
    private void handleClientDisconnect(WsSession session, int statusCode, String reason) {
        Player player = this.connectedPlayers.getOrDefault(session, null);

        if (player != null) {
            Lobby lobby = App.getLobbyManager().getLobbyById(session.pathParam("lobby-id"));
            lobby.removePlayer(player);
            this.connectedPlayers.remove(session);
        }

        System.out.println(
                "WebSocket closed for id " + session.pathParam("lobby-id") + ": (" + statusCode + ") " + reason);
    }
}
