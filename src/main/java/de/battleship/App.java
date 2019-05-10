package de.battleship;

import de.battleship.api.ApiClientHandler;
import io.javalin.Javalin;

public class App {
    private static ApiClientHandler apiClientHandler;

    private static GameManager gameManager;


    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");

        apiClientHandler = new ApiClientHandler(server);
        server.start(80);

        gameManager = new GameManager();

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

    public static ApiClientHandler getClientHandler() {
        return apiClientHandler;
    }
    public static GameManager getGameManager() {
        return gameManager;
    }
}
