/open Player.java
/open HTMLGenerator.java
/open Game.java

Player p1 = new Player("Hans");
Player p2 = new Player("Peter");

Game g = new Game(p1, p2);

assert p1.getName().equals("Hans") : "P1 should be Hans";
assert p2.getName().equals("Peter") : "P2 should be Peter";

assert g.makeTurn(0) == true;
assert checkWin() == false;
assert checkWin() == false;
assert g.makeTurn(0) == true;
assert g.makeTurn(1) == true;
assert g.makeTurn(1) == true;
assert checkWin() == false;
assert checkWin() == false;
assert g.makeTurn(2) == true;
assert g.makeTurn(2) == true;
assert g.makeTurn(3) == true;
assert checkWin() == true;
assert g.makeTurn(3) == false;
assert g.makeTurn(0) == false;

//System.out.println(g.toString());