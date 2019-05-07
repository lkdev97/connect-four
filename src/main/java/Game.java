public interface Game {

    boolean put();  //Spieler macht Zug, returns false wenn Zug ungültig
    void undo();    //Letzer (oder letzen zwei Züge) werden Rückgängig gemacht
    void newGame(); //Undo alle Züge oder erstellt neues Spiel mit neuen Spielern

}
