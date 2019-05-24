package de.battleship;

public class Player {

    String name;
    int wins;

    public Player(String name) {
        this.name = name; // habe mir überlegt, dass man in der HTML-Datei, die Spieler Namen eingeben
                          // lassen könnte und es wird angezeigt welcher Spieler dran ist
        wins = 0;
    }


    public String getName() {
        return this.name;
    }

}
