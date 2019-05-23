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
        Game testGame = new Game("A","B");

        // Game test

        server.get("/getTurn", ctx -> {
            // Testing
            int row = Integer.parseInt(ctx.queryParam("row"));
            if(testGame.makeTurn(row)){
                //return toString()
            }
            System.out.println("Row: " + ctx.queryParam("row"));
            System.out.println("Player: " + ctx.queryParam("player"));
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
