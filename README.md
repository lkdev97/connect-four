<a name="Einleitung"></a>
# Projekt: Vier Gewinnt! (Fr/2, Kr)

> Bestimmt kennen Sie alle [Vier Gewinnt](https://de.wikipedia.org/wiki/Vier_gewinnt). Hierbei handelt es sich um ein Spiel für zwei Personen, bei dem die Spieler im Wechsel 
rote und gelbe Steine in ein (in unserem Fall) 8x8-Spielfeld legen. Wer es zuerst schafft, vier Steine seiner Farbe in eine Reihe (horizontal, vertikal oder diagonal) zu bringen,
gewinnt. Unsere Anwendung ist darauf ausgelegt, das Spiel auf zwei verschiedenen Rechnern gegeneinander zu spielen. Jeder kann sich beim Betreten der Seite einen Nutzernamen 
aussuchen. Dabei können auch mehrere Spiele gleichzeitig laufen. Das funktioniert, weil die Spiele in einzelnen Lobbies stattfinden, welche entweder öffentlich oder privat sein
können. Öffentliche Lobbies können über den Spiel-Browser auf der Startseite betreten werden, während man für private Lobbies eine Spiel-ID benötigt, die man vom Ersteller der 
Lobby erhält. Diese gibt man in das vorgesehene Feld auf der Startseite ein und betritt so das Spiel. Sobald eine Lobby voll ist, kann man ihr auch als Zuschauer beitreten und
mitchatten.

#### Startseite
![Screenshot](documentation/Screenshot&#32;1.png)

#### Spielfeld
![Screenshot](documentation/Screenshot&#32;2.png)

Keywords: WebSockets, Bootstrap, Responsive Design, Server-Sent Events (SSE), JSON Mapper, Static Files, 

Projektbeteiligte:

* Marlon Drolsbach
* Lars Köhler
* Alexander Pantelkin
* Felix Schopen

#### Inhalt
- [Projekt: Vier Gewinnt! (Fr/2, Kr)](#projekt-vier-gewinnt-fr2-kr)
			- [Startseite](#startseite)
			- [Spielfeld](#spielfeld)
			- [Inhalt](#inhalt)
	- [Die Idee](#die-idee)
	- [Der Einstieg](#der-einstieg)
		- [Setup und Starten der Anwendung](#setup-und-starten-der-anwendung)
		- [UI im Browser aufrufen](#ui-im-browser-aufrufen)
		- [Erklärung der UI](#erkl%C3%A4rung-der-ui)
	- [Aufbau der Anwendung](#aufbau-der-anwendung)
		- [Die Logik in Java](#die-logik-in-java)
	- [Anleitung](#anleitung)


## Die Idee
Unser Ziel war es zunächst, eine Vier-Gewinnt-Anwendung zu schreiben. Diese wollten wir im Laufe des Projekts durch weitere Features erweitern:
* Das Spiel soll auf zwei verschiedenen Rechnern spielbar sein
* Jeder Spieler kann sich einen Namen aussuchen
* Es können mehrere Spiele gleichzeitig laufen
* Man kann sowohl private als auch öffentliche Spiele spielen
    * Private Spiele können über einen Code betreten werden
    * Öffentliche Spiele können in einem Browser eingesehen werden
* Es gibt eine Chatfunktion in jeder Lobby
* Man kann eine Lobby (sowohl öffentlich als auch privat) mit einer eigenen Lobby-Url betreten
* Man kann eine Lobby auch als Zuschauer betreten


## Der Einstieg
### Setup und Starten der Anwendung
Um die Anwendung zu starten, benötigt man das Tool [Gradle](https://gradle.org) und selbstverständlich Java. In der Konsole navigiert man in den Ordner, der 
die `build.gradle` Datei enthält. Hier führt man den Befehl `gradle run` aus. Nachdem alles Nötige heruntergeladen und kompiliert wurde, sollte nun automatisch der Javalin Server starten und das UI sollte unter `http://localhost/` erreichbar sein. 

### UI im Browser aufrufen
Als Host erreicht man die Seite im Internetbrowser
unter dem link `http://localhost/`. Andere Spieler müssen sich über die lokale [IP-Adresse]() des Hosts verbinden. Dazu müssen sie sich aber im selben 
Netzwerk wie der Host befinden. Man kann die Serveranwendung auch auf einem dedizierten Server starten, welcher dann von überall aus verfügbar ist.

### Erklärung der UI
Sobald die Seite geladen wurde, sieht man ein Fenster, in das man seinen Namen eingibt.


## Aufbau der Anwendung
### Die Logik in Java
Intern besteht das Spielfeld aus einem zweidimensionalen Array namens `field`. Ist es leer, steht an jeder Stelle eine **0**. Bei der durchführung eines Spielzuges wird an der
richtigen Stelle eine **1** für **Spieler 1** oder eine **2** für **Spieler 2** eingefügt. Dazu wird die Methode `makeTurn()` verwendet:

<details>
<summary>Methode makeTurn()</summary>

~~~java
public boolean makeTurn(int column) {
    if (!checkWin()) {
        for (int i = field[column].length - 1; i >= 0; i--) {
            if (field[column][i] == 0) {
                field[column][i] = turn + 1;
                   changeTurn();
                   return true; // true, wenn ein gültiger Zug gemacht wurde
            }
        }
    }
    return false;
}

private void changeTurn() {
    if (!checkWin()) {
        this.turn++;
        this.turn %= this.players.length;
        }
}
~~~

</details>  
<br>

Solange noch niemand gewonnen hat, wird in der gewünschten Spalte(column) der letzten Stelle, die den Wert **0** trägt, entweder **1** oder **2** zugewiesen und die Methode gibt
`true` zurück. Wenn entweder schon ein Spieler gewonnen hat oder die Spalte bereits voll ist, wird `false` zurückgegeben. Mithilfe von `changeTurn()` wechselt die Variable
`turn` jeden gültigen Spielzug zwischen den zwei Spielern (0/1).

Um zu ermitteln, ob einer der Spieler gewonnen hat, verwenden wir die Methode `checkWin()`:
<details>
<summary>Methode checkWin()</summary>

~~~java
boolean checkWin() {

    if (gameOver) {
        return true;
    }

    int currentPlayer = getTurn();

    // Prüft horizontal
    for (int j = 0; j < field.length - 3; j++) {
        for (int i = 0; i < field.length; i++) {
            if (field[i][j] == currentPlayer && field[i][j + 1] == currentPlayer && field[i][j + 2] == currentPlayer
                && field[i][j + 3] == currentPlayer) {
                
                field[i][j] = field[i][j + 1] = field[i][j + 2] = field[i][j + 3] = currentPlayer + 2;
                gameOver = true;
                winner = players[getTurn() - 1].name;
                return true;
            }
        }
    }

    
    // Dies ist eine gekürzte Version der Methode
    // In der richtigen Version folgen noch drei weitere Prüfungen für vertikal, diagonal(↗) und diagonal(↘)
    

    return false;
}
~~~

</details>
<br>

Die Variable `gameOver` vom Typ boolean wird benutzt, um zu prüfen, ob das Spiel bereits beendet ist. Wenn das nicht der Fall ist, wird mithilfe von vier verschachtelten FOR-Schleifen ermittelt,
ob vier nebeneinanderliegende Stellen dem Wert des Spielers, der am Zug ist, entsprechen. Dies geschieht horizontal, vertikal und diagonal in zwei Richtungen. Tritt dieser Fall
ein, wird die "gewinnende" Reihe mit einer `currentPlayer + 2` markiert. So erkennt man auch im Spielfeld welcher Spieler mit welcher Reihe gewonnen hat.
Nach einem abgeschlossenen Spiel könnte das Spielfeld-Array `field` beispielsweise intern so aussehen:  

~~~
0   0   0   0   0   0   2   3
0   0   0   0   0   0   2   3
0   0   0   0   0   0   2   3
0   0   0   0   0   0   0   3
0   0   0   0   0   0   0   0
0   0   0   0   0   0   0   0
0   0   0   0   0   0   0   0
0   0   0   0   0   0   0   0
~~~

Man sieht, das Spielfeld ist intern um 90° gedreht. Bei der Abbildung wird jedoch dafür gesorgt, dass es im Frontend korrekt angezeigt wird. Dafür werden die Methoden in der 
Klasse `HTMLGenerator` verwendet:

<details>
<summary>Klasse HTMLGenerator</summary>

~~~java
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
~~~

</details>
<br>

Die beiden Methoden generieren einen HTML-Code, der dem aktuellen Spielfeld entspricht. Man muss dabei beachten, dass `x` und `y` in der FOR-Schleife von
`generateBoard()` vertauscht sein müssen, damit das Spielfeld nicht um 90° gedreht angezeigt wird. 

<details>
<summary>Beispiel HTML-Code</summary>

~~~html
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light yellow-ball" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light yellow-ball" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light yellow-ball" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
<div class="board-row">
	<div class="box"><button class="btn btn-light red-ball winner" type="button" onclick="sendMove(0)"></button></div>
	<div class="box"><button class="btn btn-light red-ball winner" type="button" onclick="sendMove(1)"></button></div>
	<div class="box"><button class="btn btn-light red-ball winner" type="button" onclick="sendMove(2)"></button></div>
	<div class="box"><button class="btn btn-light red-ball winner" type="button" onclick="sendMove(3)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(4)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(5)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(6)"></button></div>
	<div class="box"><button class="btn btn-light" type="button" onclick="sendMove(7)"></button></div>
</div>
~~~

</details>
<br>

<<<<<<< Updated upstream
## Anleitung
(Für den Fall, dass jemand nicht weiß, wie das Starten der Anwendung funktioniert)
Um die Anwendung zu starten benötigt man das Tool [Gradle](https://gradle.org) und selbstverständlich Java. In der Konsole navigiert man in den Ordner, der 
die `build.gradle` Datei enthält. Hier führt man den Befehl `gradle run` aus. Das Programm sollte jetzt starten. Als Host erreicht man die Seite im Internetbrowser
unter dem link `http://localhost:80/`. Andere Spieler müssen sich über die lokale [IPv4-Adresse]() des Hosts verbinden. Dazu müssen sie sich aber im selben 
Netzwerk wie der Host befinden.

=======
>>>>>>> Stashed changes
