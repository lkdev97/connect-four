package de.battleship;

import java.util.Random;

public class Game {

    private int[][] field = new int[8][8];

    private Player p1;
    private Player p2;
    private Random r = new Random();

    Game(String player1, String player2) {
        p1 = new Player(player1);
        p2 = new Player(player2);
        p1.id = 1;
        p2.id = 2;
        p1.turn = r.nextBoolean(); // Spieler, welcher beginnt wird zufällig ausgewählt
        p2.turn = !p1.turn;
    }

    public int[][] getField() {
        return field;
    }

    public Player getP1(){
        return p1;
    }

    public Player getP2(){
        return p2;
    }

    public boolean makeTurn(int column) {
        if (!checkWin(3)) {
            if (p1.turn) {
                for (int i = field[column].length - 1; i >= 0; i--) {
                    if (field[column][i] == 0) {
                        field[column][i] = p1.id;
                        p1.turn = false;
                        p2.turn = true;
                        return checkWin(p1.id); // true, wenn p1 gewonnen hat
                    }
                }
            } else {
                for (int i = field[column].length - 1; i >= 0; i--) {
                    if (field[column][i] == 0) {
                        field[column][i] = p2.id;
                        p2.turn = false;
                        p1.turn = true;
                        return checkWin(p2.id);// true, wenn p2 gewonnen hat
                    }
                }
            }
        }
        return false;
    }

    boolean checkWin(int id) {

        // Prüft horizontal
        for (int j = 0; j < field.length - 3; j++) {
            for (int i = 0; i < field.length; i++) {
                if (field[i][j] == id && field[i][j + 1] == id && field[i][j + 2] == id && field[i][j + 3] == id) {
                    field[i][j] = field[i][j + 1] = field[i][j + 2] = field[i][j + 3] = 3; // Im field wird die Reihe
                                                                                           // mit "3" markiert
                    return true;
                }
            }
        }

        // Prüft vertikal
        for (int i = 0; i < field.length - 3; i++) {
            for (int j = 0; j < this.field.length; j++) {
                if (field[i][j] == id && field[i + 1][j] == id && field[i + 2][j] == id && field[i + 3][j] == id) {
                    field[i][j] = field[i + 1][j] = field[i + 2][j] = field[i + 3][j] = 3;// Im field wird die Reihe mit
                                                                                          // "3" markiert
                    return true;
                }
            }
        }

        // Prüft diagonal(↗)
        for (int i = 3; i < field.length; i++) {
            for (int j = 0; j < field.length - 3; j++) {
                if (field[i][j] == id && field[i - 1][j + 1] == id && field[i - 2][j + 2] == id
                        && field[i - 3][j + 3] == id) {

                    field[i][j] = field[i - 1][j + 1] = field[i - 2][j + 2] = field[i - 3][j + 3] = 3;// Im field wird
                                                                                                      // die Reihe mit
                                                                                                      // "3" markiert
                    return true;
                }
            }
        }

        // Prüft diagonal(↘)
        for (int i = 3; i < field.length; i++) {
            for (int j = 3; j < field.length; j++) {
                if (field[i][j] == id && field[i - 1][j - 1] == id && field[i - 2][j - 2] == id
                        && field[i - 3][j - 3] == id) {

                    field[i][j] = field[i - 1][j - 1] = field[i - 2][j - 2] = field[i - 3][j - 3] = 3;// Im field wird
                                                                                                      // die Reihe mit
                                                                                                      // "3" markiert
                    return true;
                }
            }
        }

        return false;
    }

    public void newGame() {

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = 0;
            }
        }
        p1.turn = r.nextBoolean();
        p2.turn = !p1.turn;

    }

    public void printField() {
        // gibt das Feld im 4Gewinnt-Style auf der Console aus
        for (int i = 0; i < 8; i++) {
            System.out.println(field[0][i] + "\t" + field[1][i] + "\t" + field[2][i] + "\t" + field[3][i] + "\t"
                    + field[4][i] + "\t" + field[5][i] + "\t" + field[6][i] + "\t" + field[7][i]);
        }
    }

    @Override
    public String toString() {
        printField();
        return "Game";
    }
}
