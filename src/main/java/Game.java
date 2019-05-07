public class Game{

    static String[][] field = new String[8][8];

    Player p1;
    Player p2;

    Game(String player1, String player2){
        p1 = new Player(player1);
        p2 = new Player(player2);
        p1.turn = true;            //Spieler 1 beginnt
        p2.turn = false;
    }

    public boolean put(int i) {
        if(p1.turn){                    //wenn p1 dran ist, macht p1 seinen Zug. Wenn der Zug gültig war werden die Turns getauscht und true zurückgegeben
            if(p1.makeTurn(i)){
                p1.turn=false;
                p2.turn=true;
                return true;
            }
            return false;               //war der Zug ungülting wird false zurückgegeben
        }
        else{
            if(p2.makeTurn(i)){        //wenn p1 nicht am zug ist, wird das ganze mit p2 gemacht
                p2.turn=false;
                p1.turn=true;
                return true;
            }
            return false;
        }
    }

    public void undo() {

    }

    public void newGame() {
        //this = new Fourwin();
    }

    void printField(){
        //gibt das Feld im 4Gewinnt-Style auf der Console aus
        for(int i = 0; i<8; i++){
            System.out.println(field[0][i]+"\t"+field[1][i]+"\t"+field[2][i]+"\t"+field[3][i]+"\t"+field[4][i]+"\t"+field[5][i]+"\t"+"\t"+field[6][i]+"\t"+field[7][i]);
        }
    }
}
