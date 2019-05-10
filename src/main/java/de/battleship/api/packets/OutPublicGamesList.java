package de.battleship.api.packets;

import java.util.Collection;

public class OutPublicGamesList extends Packet {
    public Collection<String> games;


    public OutPublicGamesList(Collection<String> publicGames) {
        this.games = publicGames;
    }
}