package de.battleship;

import java.util.ArrayList;

public class Game {

    int[][] field = new int[8][8];
    ArrayList<Integer> history = new ArrayList<>();

    Player p1;
    Player p2;

    Game(String player1, String player2){
        p1 = new Player(player1);
        p2 = new Player(player2);
        p1.id = 1;
        p2.id = 2;
        p1.turn = true;            //Spieler 1 beginnt (möglicherweise random)
        p2.turn = false;
    }

    public boolean makeTurn(int spalte) {
        if (p1.turn) {
            p1.turn = false;
            p2.turn = true;
            for (int i = this.field[spalte].length - 1; i >= 0; i--) {
                if (this.field[spalte][i] == 0) {
                    this.field[spalte][i] = this.p1.id;
                    return this.p1.hasWon();    //put gibt nur true zurück wenn einer der Spieler gewonnen hat
                }
            }
            this.p1.turn = true;                //falls Zug nicht gültig werden die Turns nicht getauscht (bzw wieder getauscht)
            this.p2.turn = false;
            return false;
        }

        else{
            this.p2.turn = false;
            this.p1.turn = true;
            for (int i = this.field[spalte].length - 1; i >= 0; i--) {
                if (this.field[spalte][i] == 0) {
                    this.field[spalte][i] = this.p2.id;
                    return this.p2.hasWon();
                }
            }
            this.p2.turn = true;
            this.p1.turn = false;
            return false;
        }
    }


    public void newGame() {
        new Game(this.p1.name, this.p2.name);
    }


    public void printField(){
        //gibt das Feld im 4Gewinnt-Style auf der Console aus
        for(int i = 0; i<8; i++){
            System.out.println(field[0][i]+"\t"+field[1][i]+"\t"+field[2][i]+"\t"+field[3][i]+"\t"+field[4][i]+"\t"+field[5][i]+"\t"+field[6][i]+"\t"+field[7][i]);
        }
    }
    @Override
    public String toString(){
        return "string";
    }
}
