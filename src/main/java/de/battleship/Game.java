package de.battleship;

import java.util.Random;

public class Game {

    private final int FIELD_X = 8;
    private final int FIELD_Y = 8;
    private int[][] field = new int[FIELD_X][FIELD_Y];

    private Player[] players;
    private Random r = new Random();
    private int turn = 0;
    private boolean gameOver;
    private Player winner = null;

    Game(Player player1, Player player2) {
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        turn = r.nextInt(players.length); // Spieler, welcher beginnt wird zufällig ausgewählt
        gameOver = false;
    }

    public int getTurn() {
        return turn + 1;
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
    public void setP1(Player p1) {
        this.players[0] = p1;
    }

    public Player getP2() {
        return this.players[1];
    }
    public void setP2(Player p2) {
        this.players[1] = p2;
    }

    public String getWinner() {
        return this.winner.name;
    }

    public void setField(int[][] field) {
        this.field = field;
    }

    private void changeTurn() {
        if (!checkWin()) {
            this.turn++;
            this.turn %= this.players.length;
        }
    }

    // True, wenn ein gültiger Zug gemacht wurde
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

    //True, wenn vier Steine in einer Reihe liegen oder wenn das Spiel bereits beendet ist
    boolean checkWin() {

        if (gameOver) {
            return true;
        }

        int currentPlayer = getTurn();

        // Prüft horizontal
        for (int j = 0; j < field.length - 3; j++) {
            for (int i = 0; i < field.length; i++) {
                if (field[i][j] == currentPlayer && field[i][j + 1] == currentPlayer && field[i][j + 2] == currentPlayer
                        && field[i][j + 3] == currentPlayer) {
                    field[i][j] = field[i][j + 1] = field[i][j + 2] = field[i][j + 3] = currentPlayer + 2;

                    gameOver = true;
                    winner = players[getTurn() - 1];

                    return true;
                }
            }
        }

        // Prüft vertikal
        for (int i = 0; i < field.length - 3; i++) {
            for (int j = 0; j < this.field.length; j++) {
                if (field[i][j] == currentPlayer && field[i + 1][j] == currentPlayer && field[i + 2][j] == currentPlayer
                        && field[i + 3][j] == currentPlayer) {
                    field[i][j] = field[i + 1][j] = field[i + 2][j] = field[i + 3][j] = currentPlayer + 2;

                    gameOver = true;
                    winner = players[getTurn() - 1];

                    return true;
                }
            }
        }

        // Prüft diagonal(↗)
        for (int i = 3; i < field.length; i++) {
            for (int j = 0; j < field.length - 3; j++) {
                if (field[i][j] == currentPlayer && field[i - 1][j + 1] == currentPlayer
                        && field[i - 2][j + 2] == currentPlayer && field[i - 3][j + 3] == currentPlayer) {

                    field[i][j] = field[i - 1][j + 1] = field[i - 2][j + 2] = field[i - 3][j + 3] = currentPlayer + 2;

                    gameOver = true;
                    winner = players[getTurn() - 1];

                    return true;
                }
            }
        }

        // Prüft diagonal(↘)
        for (int i = 3; i < field.length; i++) {
            for (int j = 3; j < field.length; j++) {
                if (field[i][j] == currentPlayer && field[i - 1][j - 1] == currentPlayer
                        && field[i - 2][j - 2] == currentPlayer && field[i - 3][j - 3] == currentPlayer) {

                    field[i][j] = field[i - 1][j - 1] = field[i - 2][j - 2] = field[i - 3][j - 3] = currentPlayer + 2;

                    gameOver = true;
                    winner = players[getTurn() - 1];
                    return true;
                }
            }
        }

        return false;
    }

    // Spielfeld wird zurückgesetzt
    public void newGame() {
        field = new int[FIELD_X][FIELD_Y];
        gameOver = false;
        turn = r.nextInt(players.length);
    }

    // Gibt das Feld im 4Gewinnt-Style auf der Konsole aus
    public void printField() {
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
