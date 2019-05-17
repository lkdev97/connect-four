/open Player.java
/open HTMLGenerator.java
/open Game.java

Game g = new Game("Hans", "Peter");

assert g.makeTurn(0) == false;
assert g.makeTurn(0) == false;
assert g.makeTurn(1) == false;
assert g.makeTurn(1) == false;
assert g.makeTurn(2) == false;
assert g.makeTurn(2) == false;
assert g.makeTurn(3) == true;
assert g.makeTurn(3) == false;
assert g.makeTurn(0) == false;