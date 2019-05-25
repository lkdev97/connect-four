package de.battleship;

import io.javalin.websocket.WsSession;

public class OnlinePlayer extends Player {
    private WsSession session;


    public OnlinePlayer(String name, WsSession session) {
        super(name);
        this.session = session;
    }


    public WsSession getSession() {
        return this.session;
    }
}
