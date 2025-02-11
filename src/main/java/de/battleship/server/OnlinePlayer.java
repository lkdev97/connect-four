package de.battleship.server;

import java.util.Objects;

import de.battleship.Player;
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
    public void sendErrorMessage(String message) {
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
        if (this.isConnected()) {
            if (reason != null)
                this.sendErrorMessage(reason);
            
            this.session.close();
        }
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

    /**
     * Setzt einen neuen Spielernamen für diesen Spieler.
     */
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.wins, this.session.getId());
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof OnlinePlayer && ((OnlinePlayer) obj).session.getId().equals(this.session.getId());
    }
}
