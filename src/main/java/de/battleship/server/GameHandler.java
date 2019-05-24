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
            ws.onConnect(session -> {
                Game game = App.getGameManager().getGameById(session.pathParam("game-id"));

                if (game == null) {
                    session.send(new OutError("Game not found.").toString());
                    session.close(1, "Disconnect by server.");
                }
            });
            ws.onMessage(this::handleClientMessage);
            ws.onClose((session, statusCode, reason) -> {
                // TODO: Remove player from lobby
                System.out.println(
                        "WebSocket closed for id " + session.pathParam("game-id") + ": (" + statusCode + ") " + reason);
            });
        });
    }


    private void handleClientMessage(WsSession session, String message) {
        Game game = App.getGameManager().getGameById(session.pathParam("game-id"));
                System.out.println(session.pathParam("game-id") + " >> " + message);

                this.sendPacket(session, new OutGameField(game.toString()));
    }
    

    /**
     * Sendet ein Packet an den Client.
     */
    private void sendPacket(WsSession session, GamePacket packet) {
        session.send(packet.toString());
    }
}
