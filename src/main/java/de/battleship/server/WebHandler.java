package de.battleship.server;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.battleship.App;
import de.battleship.Lobby;
import de.battleship.server.packets.web.InCreateLobby;
import de.battleship.server.packets.web.OutCreateLobby;
import de.battleship.server.packets.web.OutError;
import de.battleship.server.packets.web.OutPublicLobbiesList;
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
     * Eine Liste, die alle SseClients speichert, welche die Lobby-Browser Events abhören.
     */
    private ArrayList<SseClient> lobbiesListEventClients;


    private ObjectMapper jsonConverter;


    public WebHandler(Javalin server) {
        this.server = server;
        this.lobbiesListEventClients = new ArrayList<SseClient>();
        this.jsonConverter = new ObjectMapper();

        this.server.post("/create", this::handleCreateLobby);
        this.server.post("/lobbylist", this::handlePublicLobbiesList);
        this.server.sse("/lobbylist", client -> {
            client.onClose(() -> this.lobbiesListEventClients.remove(client));
            this.lobbiesListEventClients.add(client);
        });
    }

    public void broadcastNewPublicLobby(Lobby lobby) {
        try {
            String jsonData = this.jsonConverter.writeValueAsString(lobby.getData());

            for (int i = this.lobbiesListEventClients.size() - 1; i >= 0; i--)
                this.lobbiesListEventClients.get(i).sendEvent("addlobby", jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void broadcastUpdatePublicLobby(Lobby lobby) {
        try {
            String jsonData = this.jsonConverter.writeValueAsString(lobby.getData());

            for (int i = this.lobbiesListEventClients.size() - 1; i >= 0; i--)
                this.lobbiesListEventClients.get(i).sendEvent("updlobby", jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void broadcastRemovePublicLobby(Lobby lobby) {
        try {
            String jsonData = this.jsonConverter.writeValueAsString(lobby.getData());

            for (int i = this.lobbiesListEventClients.size() - 1; i >= 0; i--)
                this.lobbiesListEventClients.get(i).sendEvent("rmlobby", jsonData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Wird aufgerufen, wenn der Client eine neue Lobby erstellen möchte.
     */
    private void handleCreateLobby(Context ctx) {
        try {
            InCreateLobby in = ctx.bodyAsClass(InCreateLobby.class);
            this.sendPacket(ctx, new OutCreateLobby(App.getLobbyManager().createNewLobby(in.isPublic)));
        } catch (Exception ex) {
            this.sendError(ctx, "Invalid request.");
        }
    }

    private void handlePublicLobbiesList(Context ctx) {
        this.sendPacket(ctx, new OutPublicLobbiesList(App.getLobbyManager().getPublicLobbies()));
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
