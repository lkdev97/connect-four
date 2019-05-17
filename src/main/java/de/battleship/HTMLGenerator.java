package de.battleship;

public final class HTMLGenerator {
    public static String generateBoard(int[][] content) {
        StringBuilder boardString = new StringBuilder();
        
        for (int y = 0; y < content.length; y++) {
            boardString.append("<div class=\"board-row\">\n");
            for (int x = 0; x < content[y].length; x++)
                boardString.append(generateCellElement(content[x][y]));
            boardString.append("</div>\n");
        }

        return boardString.toString();
    }

    private static String generateCellElement(int playerId) {
        return "\t<div class=\"box"
                + (playerId == 2 ? " red-ball" : playerId == 1 ? " yellow-ball" : playerId == 3 ? " green-ball" : "")
                + "\"></div>\n";
    }
}