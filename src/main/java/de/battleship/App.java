package de.battleship;

import de.battleship.server.GameHandler;
import de.battleship.server.Lobby;
import de.battleship.server.LobbyManager;
import de.battleship.server.WebHandler;
import io.javalin.Javalin;

public class App {
    private static WebHandler webHandler;
    private static GameHandler gameHandler;

    private static LobbyManager lobbyManager;

    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");
        server.enableCaseSensitiveUrls();

        webHandler = new WebHandler(server);
        gameHandler = new GameHandler(server);
        server.start(80);

        lobbyManager = new LobbyManager();

        
        server.get("/getTurn", ctx -> {
            int row = Integer.parseInt(ctx.queryParam("row"));
            int player = Integer.parseInt(ctx.queryParam("player"));
            String lobbyId = ctx.queryParam("id");
            Game g = lobbyManager.getLobbyById(lobbyId).getGame();

            if (g != null) {
                if (player == g.getTurn())
                    g.makeTurn(row);

                ctx.result("Current turn: " + g.getTurn() + "\n" + g.toString());
            } else
                ctx.result("Lobby with id " + lobbyId + " does not exist.");
        });

        server.get("/newgame", ctx -> {
            String lobbyId = ctx.queryParam("id");
            Lobby lobby = lobbyManager.getLobbyById(lobbyId);

            if (lobby != null) {
                Game g = lobby.getGame();

                g.newGame();
                ctx.result(g.toString());
            } else {
                ctx.result("New lobby id: " + lobbyManager.createNewLobby(false));
            }
        });

    }

    public static WebHandler getWebHandler() {
        return webHandler;
    }
    public static GameHandler getGameHandler() {
        return gameHandler;
    }

    public static LobbyManager getLobbyManager() {
        return lobbyManager;
    }
}
