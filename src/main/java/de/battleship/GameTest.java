/open Player.java
/open HTMLGenerator.java
/open Game.java

Player p1 = new Player("Hans");
Player p2 = new Player("Peter");

Game g = new Game(p1, p2);

assert p1.getName().equals("Hans") : "P1 should be Hans";
assert p2.getName().equals("Peter") : "P2 should be Peter";

assert g.makeTurn(0) : "Test (1)";
assert !g.checkWin() : "Test (2)";
assert !g.checkWin() : "Test (3)";
assert g.makeTurn(0) : "Test (4)";
assert g.makeTurn(1) : "Test (5)";
assert g.makeTurn(1) : "Test (6)";
assert !g.checkWin() : "Test (7)";
assert !g.checkWin() : "Test (8)";
assert g.makeTurn(2) : "Test (9)";
assert g.makeTurn(2) : "Test (10)";
assert g.makeTurn(3) : "Test (11)";
assert g.checkWin() : "Test (12)";
assert g.getWinner().equals(p1.name) || g.getWinner().equals(p2.name) : "Test (13)";
assert !g.makeTurn(3) : "Test (14)";
assert !g.makeTurn(0) : "Test (15)";

g.newGame();

assert !g.checkWin() : "Test (15)";
assert g.makeTurn(0) : "Test (16)";
assert !g.checkWin() : "Test (17)";
