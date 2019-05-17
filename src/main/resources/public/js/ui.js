var http = new XMLHttpRequest();
var player1 = true; //fängt an
var player2 = false;

function sendRequestGET(path = '', query = '') {
    http.open('GET', path + '?' + query);
    http.send();
}

http.onreadystatechange = function () {
    if (this.readyState == 4 && this.status == 200 && this.responseText) {
        console.log(this.response);
    }
}


/*
* die Funktion erstellt über eine for-Schleife das Spielfeld "board"
* erst wird das Element über die id geholt und in der for-Schleife wird das Feld dupliziert bis das Feld 64 Felder hat
* jedem Feld wird gesagt in welcher Spalte es liegt. Die Spalte wird in dem id von dem box-Element gespeichert
* 
*       <div id="box" class="box">
*            <button id=2 type="button" onclick="sendRow(this)" class="btn btn-light"></button>
*        </div>
*/
function loadBoardContent() {
    var box = document.getElementById("box");
    var html = "";
    var row_counter = 1;
    for (var i = 1; i < 65; i++) {
        if (row_counter > 8) row_counter = 1;
        box.firstElementChild.id = row_counter;
        html += box.parentElement.innerHTML;
        row_counter++;
    }
    document.getElementById("board").innerHTML = html;
}

/*
* Wenn man auf ein Feld "box" klickt wird die Spalte übergeben
*
*       <div id="box" class="box">
*            <button id=2 type="button" onclick="sendRow(this)" class="btn btn-light"></button>
*        </div>
*/
function sendRow(row) {
    if (player1) {
        row.classList.add('red-ball');
        player1 = false;
        player2 = true;
        sendRequestGET('getTurn', "row=" + row.id + "&player=1");
    } else if (player2) {
        player1 = true;
        player2 = false;
        row.classList.add('yellow-ball');
        sendRequestGET('getTurn', "row=" + row.id + "&player=2");
    }
}

/**
 * Sobald man einem bestehenden Spiel beitritt oder eins erstellt wird das Spielfeld angezeigt
 * 
 */
function showBoard(el) {
    el.classList.add('is--hidden');
    el.parentElement.parentElement.classList.add('is--hidden');
    document.getElementById('board').classList.remove('is--hidden');
    document.getElementById('context-box').classList.add('is--hidden');
    var player = prompt("Geben Sie ihren Namen an", "Player1");
    document.getElementById("game-url").innerHTML = "Viel Erfolg " + player + "!";
    //sendRequestGET('playerName', player);
    document.getElementById("new-game").classList.remove('is--hidden');
}