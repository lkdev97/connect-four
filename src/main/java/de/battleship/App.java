package de.battleship;

import de.battleship.server.WebHandler;
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

        server.ws("/:game-id", ws -> {
            
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