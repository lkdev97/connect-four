package de.battleship;

import de.battleship.server.WebHandler;
import de.battleship.server.packets.game.OutError;
import de.battleship.server.packets.game.OutGameField;
import io.javalin.Javalin;

public class App {
    private static WebHandler webHandler;

    private static GameManager gameManager;

    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");
        server.enableCaseSensitiveUrls();

        webHandler = new WebHandler(server);
        server.start(80);

        gameManager = new GameManager();


        // Game Websocket test
        server.ws("/:game-id", ws -> {
            ws.onConnect(session -> {
                Game game = gameManager.getGameById(session.pathParam("game-id"));

                if (game == null) {
                    session.send(new OutError("Game not found.").toString());
                    session.close(1, "Disconnect by server.");
                }
            });

            ws.onMessage((session, message) -> {
                Game game = gameManager.getGameById(session.pathParam("game-id"));
                System.out.println(session.pathParam("game-id") + " >> " + message);

                session.send(new OutGameField(game.toString()).toString());

                //session.send("{\"gameField\":\"" + game.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\t", "\\t") + "\"}");
            });

            ws.onClose((session, statusCode, reason) -> {
                System.out.println("WebSocket closed for id " + session.pathParam("game-id") + ": (" + statusCode + ") " + reason);
            });
        });

        // Game test
        server.get("/getTurn", ctx -> {
            // Testing
            int row = Integer.parseInt(ctx.queryParam("row"));
            int player = Integer.parseInt(ctx.queryParam("player"));
            String gameId = ctx.queryParam("id");
            Game g = gameManager.getGameById(gameId);

            if (g != null) {
                if (player == g.getTurn())
                    g.makeTurn(row);

                ctx.result("Current turn: " + g.getTurn() + "\n" + g.toString());
            } else
                ctx.result("Game with id " + gameId + " does not exist.");
        });

        server.get("/newgame", ctx -> {
            String gameId = ctx.queryParam("id");
            Game g = gameManager.getGameById(gameId);

            if (g != null) {
                g.newGame();
                ctx.result(g.toString());
            } else {
                ctx.result("New game id: " + gameManager.createNewGame(false));
            }
        });

    }

    public static WebHandler getWebHandler() {
        return webHandler;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}