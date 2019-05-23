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
        Game testGame = new Game("A", "B");

        // Game test

        server.get("/getTurn", ctx -> {
            // Testing
            int row = Integer.parseInt(ctx.queryParam("row"));
            int player = Integer.parseInt(ctx.queryParam("player"));

            if (player == testGame.getTurn()) {

                testGame.makeTurn(row);

                if (testGame.checkWin(player)) {
                    // return player has won
                }

                ctx.result(testGame.toString());
            }
        });

        server.get("/newgame", ctx -> {

        });

    }

    public static WebApiHandler getWebApiHandler() {
        return webApiHandler;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
