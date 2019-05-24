package de.battleship.server.packets.web;

import java.util.Collection;

public class OutPublicGamesList extends WebPacket {
    public Collection<String> games;


    public OutPublicGamesList(Collection<String> publicGames) {
        this.games = publicGames;
    }
}