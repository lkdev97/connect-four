package de.battleship;

import de.battleship.api.WebApiHandler;
import io.javalin.Javalin;

public class App {
    private static WebApiHandler webApiHandler;

    private static GameManager gameManager;


    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");

        webApiHandler = new WebApiHandler(server);
        server.start(80);

        gameManager = new GameManager();

        //Game test

        Game g1 = new Game("1111", "2222");
        g1.makeTurn(0);
        g1.makeTurn(0);
        g1.makeTurn(6);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(7);
        g1.makeTurn(5);
        g1.makeTurn(5);
        g1.printField();

    }

    public static WebApiHandler getWebApiHandler() {
        return webApiHandler;
    }
    public static GameManager getGameManager() {
        return gameManager;
    }
}
