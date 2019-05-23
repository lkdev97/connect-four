package de.battleship;

import java.util.Random;

public class Game {

    private int[][] field = new int[8][8];

    private Player[] players;
    private Random r = new Random();
    private int turn = 0;

    Game(Player player1, Player player2) {
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        turn = r.nextInt(players.length); // Spieler, welcher beginnt wird zufällig ausgewählt
    }

    public int getTurn() {
        return turn;
    }
    public Player getCurrentPlayer() {
        return this.players[this.turn];
    }

    public int[][] getField() {
        return field;
    }

    public Player getP1() {
        return this.players[0];
    }

    public Player getP2() {
        return this.players[1];
    }

    public void setField(int[][] field){
        this.field=field;
    }

    private void changeTurn() {
        if (!checkWin()) {
            this.turn++;
            this.turn %= this.players.length;
        }
    }

    public boolean makeTurn(int column) {
        if (!checkWin()) {
            for (int i = field[column].length - 1; i >= 0; i--) {
                if (field[column][i] == 0) {
                    field[column][i] = turn + 1;
                    changeTurn();
                    return true; // true, wenn ein gültiger Zug gemacht wurde
                }
            }
        }
        return false;
    }

    boolean checkWin() {

        // Prüft horizontal
        for (int j = 0; j < field.length - 3; j++) {
            for (int i = 0; i < field.length; i++) {
                if (field[i][j] == turn && field[i][j + 1] == turn && field[i][j + 2] == turn && field[i][j + 3] == turn) {
                    field[i][j] = field[i][j + 1] = field[i][j + 2] = field[i][j + 3] = 3; // Im field wird die Reihe
                                                                                           // mit "3" markiert
                    turn = 3;
                    return true;
                }
            }
        }

        // Prüft vertikal
        for (int i = 0; i < field.length - 3; i++) {
            for (int j = 0; j < this.field.length; j++) {
                if (field[i][j] == turn && field[i + 1][j] == turn && field[i + 2][j] == turn && field[i + 3][j] == turn) {
                    field[i][j] = field[i + 1][j] = field[i + 2][j] = field[i + 3][j] = 3;// Im field wird die Reihe mit
                                                                                          // "3" markiert
                    turn = 3;
                    return true;
                }
            }
        }

        // Prüft diagonal(↗)
        for (int i = 3; i < field.length; i++) {
            for (int j = 0; j < field.length - 3; j++) {
                if (field[i][j] == turn && field[i - 1][j + 1] == turn && field[i - 2][j + 2] == turn
                        && field[i - 3][j + 3] == turn) {

                    field[i][j] = field[i - 1][j + 1] = field[i - 2][j + 2] = field[i - 3][j + 3] = 3;// Im field wird
                                                                                                      // die Reihe mit
                                                                                                      // "3" markiert
                    turn = 3;
                    return true;
                }
            }
        }

        // Prüft diagonal(↘)
        for (int i = 3; i < field.length; i++) {
            for (int j = 3; j < field.length; j++) {
                if (field[i][j] == turn && field[i - 1][j - 1] == turn && field[i - 2][j - 2] == turn
                        && field[i - 3][j - 3] == turn) {

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
        field = new int[8][8];
        turn = r.nextInt(players.length); // Spieler, welcher beginnt wird zufällig ausgewählt
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
        return HTMLGenerator.generateBoard(field);
    }
}
