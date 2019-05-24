/*
 * Beinhaltet alle Funktionen für das UI.
 */


// Referenzen auf alle wichtigen/funktionalen HTML UI Elemente
let createGameButton;
let createGameIsPublicBox;
let joinGameButton;
let joinGameId;
let lobbyBrowser;
let lobbyCounter;
let board;


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
    console.log("c");
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


function initUI() {
    loadBoardContent();
    updateUIReferences();
}

/**
 * Sobald man einem bestehenden Spiel beitritt oder eins erstellt wird das Spielfeld angezeigt
 * 
 */
function showBoard() {
    board.classList.add('is--hidden');
    board.parentElement.classList.add('is--hidden');
    document.getElementById('board').classList.remove('is--hidden');
    document.getElementById('join-game-wrapper').classList.add('is--hidden');
    document.getElementById('browse-lobbies-wrapper').classList.add('is--hidden');
    document.getElementById("new-game").classList.remove('is--hidden');
    document.getElementById("leave-lobby").classList.remove('is--hidden');

    document.getElementById("game-url").innerHTML = "Viel Erfolg " + playerName + "!";
}

// Setzt den HTML Inhalt für das Spielfeld.
function setBoardContent(htmlContent) {

}



// Lädt Referenzen auf UI Elemente neu.
function updateUIReferences() {
    createGameButton = document.getElementById('create-game-button');
    createGameIsPublicBox = document.getElementById('create-game-public-box');
    joinGameButton = document.getElementById('join-game-button');
    joinGameId = document.getElementById('join-game-code');
    lobbyBrowser = document.getElementById('lobby-browser').querySelector('tbody');
    lobbyCounter = document.getElementById('lobby-counter');
    board = document.getElementById('board');

    return createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && lobbyBrowser && lobbyCounter && board;
}

// Fügt ein Spiel mit der angegebenen ID in die Spieleliste hinzu.
function addGameToBrowser(gameId) {
    if (!lobbyBrowser.querySelector(`#entry-${gameId}`)) {
        // erstellt die Tabellenreihe
        let rowElement = document.createElement('tr');
        rowElement.id = gameId;

        // erstellt den ID-Text
        let idTextElement = document.createElement('td');
        idTextElement.innerText = gameId;
        // erstellt den Spielerzahl-Text
        let playerNumTextElement = document.createElement('td');
        playerNumTextElement.innerText = '0 / 2';
        // erstellt den Link, mit dem man sich zum Spiel verbinden kann
        let joinTextElement = document.createElement('td');
        let joinLinkElement = document.createElement('span');
        joinLinkElement.className = 'link';
        joinLinkElement.innerText = '>> Beitreten';
        joinLinkElement.addEventListener('click', () => joinGame(gameId));
        joinTextElement.appendChild(joinLinkElement);

        // fügt die einzelnen Elemente in die Tabellenreihe ein
        rowElement.appendChild(idTextElement);
        rowElement.appendChild(playerNumTextElement);
        rowElement.appendChild(joinTextElement);

        lobbyBrowser.insertBefore(rowElement, lobbyBrowser.firstChild);

        lobbyCounter.innerText = lobbyBrowser.children.length;
    }
}
// Löscht ein Spiel mit der angegebenen ID aus der Spieleliste.
function removeGameFromBrowser(gameId) {
    let row = lobbyBrowser.querySelector(`#entry-${gameId}`);
    if (row)
        row.remove();

    lobbyCounter.innerText = lobbyBrowser.children.length;
}
// Löscht alle Einträge aus der Spieleliste.
function clearGameBrowser() {
    while (lobbyBrowser.firstChild)
        lobbyBrowser.firstChild.remove();

    lobbyCounter.innerText = lobbyBrowser.children.length;
}
