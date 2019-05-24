/open Player.java
/open HTMLGenerator.java
/open Game.java

Player p1 = new Player("Hans");
Player p2 = new Player("Peter");

Game g = new Game(p1, p2);

assert p1.getName().equals("Hans") : "P1 should be Hans";
assert p2.getName().equals("Peter") : "P2 should be Peter";

assert g.makeTurn(0) == true : "Test (1)";
assert g.checkWin() == false : "Test (2)";
assert g.checkWin() == false : "Test (3)";
assert g.makeTurn(0) == true : "Test (4)";
assert g.makeTurn(1) == true : "Test (5)";
assert g.makeTurn(1) == true : "Test (6)";
assert g.checkWin() == false : "Test (7)";
assert g.checkWin() == false : "Test (8)";
assert g.makeTurn(2) == true : "Test (9)";
assert g.makeTurn(2) == true : "Test (10)";
assert g.makeTurn(3) == true : "Test (11)";
assert g.checkWin() == true : "Test (12)";
assert g.makeTurn(3) == false : "Test (13)";
assert g.makeTurn(0) == false : "Test (14)";

g.newGame();

assert g.checkWin() == false: "Test (15)";

//System.out.println(g.toString());