/open Player.java
/open HTMLGenerator.java
/open Game.java

Game g = new Game("Hans", "Peter");

assert g.makeTurn(0) == true;
assert checkWin(g.getP1().id) == false;
assert checkWin(g.getP2().id) == false;
assert g.makeTurn(0) == true;
assert g.makeTurn(1) == true;
assert g.makeTurn(1) == true;
assert checkWin(g.getP1().id) == false;
assert checkWin(g.getP2().id) == false;
assert g.makeTurn(2) == true;
assert g.makeTurn(2) == true;
assert g.makeTurn(3) == true;
assert checkWin(g.getP1().id) == true;
assert checkWin(g.getP2().id) == false;
assert g.makeTurn(3) == false;
assert g.makeTurn(0) == false;

//System.out.println(g.toString());