package de.battleship;

public class Player {

    String name;
    boolean turn;

    Player(String name){
        this.name=name; //habe mir überlegt, dass man in der HTML-Datei, die Spieler Namen eingeben lassen könnte und es wird angezeigt welcher Spieler dran ist
    }

    //Prüft nach jedem Zug ob einer der Spieler gewonnen hat
    boolean hasWon(){
        return false;
    }

    public boolean makeTurn(int spalte) {
        for(int i = Game.field[spalte].length-1; i>=0; i--) {
            if (Game.field[spalte][i] == null) {
                Game.field[spalte][i] = this.name;
                return true;
            }
        }
        return false;
    }

    public void undo() {

    }

}
