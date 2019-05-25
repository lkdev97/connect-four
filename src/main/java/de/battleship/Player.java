package de.battleship;

import java.util.Objects;

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


    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.wins);
    }
    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Player && ((Player) obj).name.equals(this.name);
    }
}
