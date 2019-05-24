package de.battleship;

public class Player {

    String name;
    int wins;

    public Player(String name) {
        this.name = name;
        wins = 0;
    }


    public String getName() {
        return this.name;
    }

}
