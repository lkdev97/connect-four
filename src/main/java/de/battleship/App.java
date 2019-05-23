package de.battleship;

import de.battleship.api.WebApiHandler;
import io.javalin.Javalin;

public class App {
    private static WebApiHandler webApiHandler;

    private static GameManager gameManager;

    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");
        server.enableCaseSensitiveUrls();

        webApiHandler = new WebApiHandler(server);
        server.start(80);

        gameManager = new GameManager();

        // Game test

        server.get("/getTurn", ctx -> {
            // Testing
            int row = Integer.parseInt(ctx.queryParam("row"));
            int player = Integer.parseInt(ctx.queryParam("player"));
            String gameId = ctx.queryParam("id");
            Game g = gameManager.getGameById(gameId);

            if (g != null) {

                if (player == g.getTurn()) {

                    g.makeTurn(row);
                    gameManager.getGameById(gameId).setField(g.getField());

                    if (g.checkWin(player)) {
                        ctx.result(g.toString());
                    }

                    ctx.result(g.toString());
                }
            }
        });

        server.get("/newgame", ctx -> {
            String gameId = ctx.queryParam("id");
            Game g = gameManager.getGameById(gameId);

            if (g != null) {
                g.newGame();
                ctx.result(g.toString());
            }
        });

    }

    public static WebApiHandler getWebApiHandler() {
        return webApiHandler;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}