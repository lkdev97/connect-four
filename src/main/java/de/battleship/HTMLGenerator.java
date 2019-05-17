package de.battleship;

public final class HTMLGenerator {
    public static String generateBoard(int[][] content) {
        StringBuilder boardString = new StringBuilder();
        for (int y = 0; y < content.length; y++)
            for (int x = 0; x < content[y].length; x++)
                boardString.append(generateCellElement(content[y][x]));

        return boardString.toString();
    }


    private static String generateCellElement(int playerId) {
        return "<div class=\"box" + (playerId == 2 ? " red-ball" : playerId == 1 ? " yellow-ball" : playerId == 3 ? " green-ball" : "") + "\"></div>";
    }
}