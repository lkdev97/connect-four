package de.battleship;

import de.battleship.server.packets.game.GamePacket;
import de.battleship.server.packets.game.OutError;
import io.javalin.websocket.WsSession;

/**
 * Stellt einen zum Server verbundenen Spieler dar.
 */
public class OnlinePlayer extends Player {
    /**
     * Speichert die WebSocket-Session des Spielers.
     */
    private WsSession session;


    public OnlinePlayer(String name, WsSession session) {
        super(name);
        this.session = session;
    }


    /**
     * Sendet ein Packet an den Spieler.
     */
    public void sendPacket(GamePacket packet) {
        this.session.send(packet.toString());
    }
    
    /**
     * Sendet eine Fehlernachricht an den Spieler.
     */
    public void sendErrorMessage(WsSession session, String message) {
        this.sendPacket(new OutError(message));
    }


    /**
     * Trennt die Verbindung zum Spieler.
     */
    public void disconnect() {
        this.disconnect(null);
    }
    /**
     * Trennt die Verbindung zum Spieler mit einem angegebenen Grund.
     */
    public void disconnect(String reason) {
        if (this.isConnected())
            this.session.close(1, reason);
    }


    /**
     * Sagt aus, ob der Spieler mit dem Server verbunden ist.
     */
    public boolean isConnected() {
        return this.session.isOpen();
    }

    /**
     * Gibt die WebSocket-Session des Spielers zurück.
     */
    public WsSession getSession() {
        return this.session;
    }
}
