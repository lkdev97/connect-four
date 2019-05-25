package de.battleship;

public class HTMLGenerator {
    public static String generateBoard(int[][] content) {
        StringBuilder boardString = new StringBuilder();

        for (int y = 0; y < content.length; y++) {
            boardString.append("<div class=\"board-row\">\n");
            for (int x = 0; x < content[y].length; x++)
                boardString.append(generateCellElement(x, content[x][y]));
            boardString.append("</div>\n");
        }

        return boardString.toString();
    }

    private static String generateCellElement(int column, int playerId) {
        return "\t<div class=\"box\">"
                +   "<button class=\"btn btn-light"
                +   (playerId == 1 ? " yellow-ball"
                            : playerId == 2 ? " red-ball"
                                    : playerId == 3 ? " yellow-ball winner" 
                                        : playerId == 4 ? " red-ball winner" : "")
                +   "\" type=\"button\" onclick=\"sendMove(" + column + ")\"></button>" 
                + "</div>\n";
    }
}