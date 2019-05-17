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

        //Game test

        server.get("/getTurn", ctx -> {
            //Testing
            System.out.println(ctx.queryParam("row"));
            System.out.println(ctx.queryParam("player"));
        });

    }

    public static WebApiHandler getWebApiHandler() {
        return webApiHandler;
    }
    public static GameManager getGameManager() {
        return gameManager;
    }
}
