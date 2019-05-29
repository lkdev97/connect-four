/*
 * Beinhaltet alle Funktionen für das UI.
 */


// Referenzen auf alle wichtigen/funktionalen HTML UI Elemente
let createGameButton;
let createGameIsPublicBox;
let joinGameButton;
let joinGameId;
let leaveGameButton;
let lobbyBrowser;
let lobbyCounter;
let board;
let txtUserName;



/**
 * Erhält das Spielfeld über die toString-Methode im HTML-Format
 */
function setBoardContent(content) {
    board.innerHTML = content;
}
/**
 * Löscht den Inhalt des Spielfeldes.
 */
function clearBoardContent() {
    setBoardContent('');
}


function setUserName(username) {
    playerName = username;
    document.getElementById("username").innerHTML = playerName;
}

function changeUserName() {
    setUserName(prompt("Spielname ändern", playerName) || playerName);
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
// Diese Funktion wird nicht mehr benötigt, da der Server dem Client den Inhalt des Feldes sendet.
/*function loadBoardContent() {
    var box = document.getElementById("box");
    var html = "";
    var row_counter = 1;
    for (var i = 1; i < 65; i++) {
        if (row_counter > 8) row_counter = 1;
        box.firstElementChild.id = row_counter;
        html += box.parentElement.innerHTML;
        row_counter++;
    }
    board.innerHTML = html;
}*/


function initUI() {
    if (updateUIReferences()) {
        leaveGameButton.addEventListener('click', disconnectFromGame);
        return true;
    }

    return false;
}

/**
 * Sobald man einem bestehenden Spiel beitritt oder eins erstellt wird das Spielfeld angezeigt
 * 
 */
function showBoard(gameId) {
    board.classList.add('is--hidden');
    board.parentElement.classList.add('is--hidden');
    document.getElementById('board').classList.remove('is--hidden');
    document.getElementById('join-game-wrapper').classList.add('is--hidden');
    document.getElementById('browse-lobbies-wrapper').classList.add('is--hidden');
    document.getElementById("new-game").classList.remove('is--hidden');
    document.getElementById("leave-lobby").classList.remove('is--hidden');
    document.getElementById("url-box").classList.remove('is--hidden');

    document.getElementById("game-url").innerHTML = "Viel Erfolg " + playerName + "! <br> Spiel-ID: " + gameId;
}

/**
 * Sobald man auf den Button "Leave Lobby" klickt, wird das Spielfeld ausgeblendet und man landet wieder auf der Startseite
 */
function hideBoard() {
    board.classList.remove('is--hidden');
    board.parentElement.classList.remove('is--hidden');
    document.getElementById('board').classList.add('is--hidden');
    document.getElementById('join-game-wrapper').classList.remove('is--hidden');
    document.getElementById('browse-lobbies-wrapper').classList.remove('is--hidden');
    document.getElementById("new-game").classList.add('is--hidden');
    document.getElementById("leave-lobby").classList.add('is--hidden');
    document.getElementById("url-box").classList.add('is--hidden');
}



// Lädt Referenzen auf UI Elemente neu.
function updateUIReferences() {
    createGameButton = document.getElementById('create-game-button');
    txtUserName = document.getElementById('username');
    createGameIsPublicBox = document.getElementById('create-game-public-box');
    joinGameButton = document.getElementById('join-game-button');
    joinGameId = document.getElementById('join-game-code');
    leaveGameButton = document.getElementById('leave-lobby');
    lobbyBrowser = document.getElementById('lobby-browser').querySelector('tbody');
    lobbyCounter = document.getElementById('lobby-counter');
    board = document.getElementById('board');

    return createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && leaveGameButton && lobbyBrowser && lobbyCounter && board;
}

// Fügt eine Lobby mit den angegebenen Daten zur Lobbyliste hinzu.
function addLobbyToBrowser(lobbyData) {
    if (!lobbyBrowser.querySelector(`#entry-${lobbyData.lobbyId}`)) {
        // erstellt die Tabellenreihe
        let rowElement = document.createElement('tr');
        rowElement.id = `entry-${lobbyData.lobbyId}`;

        // erstellt den ID-Text
        let idTextElement = document.createElement('td');
        idTextElement.innerText = lobbyData.lobbyId;
        // erstellt den Spielerzahl-Text
        let playerNumTextElement = document.createElement('td');
        playerNumTextElement.classList.add('player-num-text');
        playerNumTextElement.innerText = `${lobbyData.players} / ${lobbyData.maxPlayers}`;
        // erstellt den Link, mit dem man sich zum Spiel verbinden kann
        let joinTextElement = document.createElement('td');
        let joinLinkElement = document.createElement('span');
        joinLinkElement.className = 'link';
        joinLinkElement.innerText = '>> Beitreten';
        joinLinkElement.addEventListener('click', () => joinGame(lobbyData.lobbyId));
        joinTextElement.appendChild(joinLinkElement);

        // fügt die einzelnen Elemente in die Tabellenreihe ein
        rowElement.appendChild(idTextElement);
        rowElement.appendChild(playerNumTextElement);
        rowElement.appendChild(joinTextElement);

        lobbyBrowser.insertBefore(rowElement, lobbyBrowser.firstChild);

        lobbyCounter.innerText = lobbyBrowser.children.length;
    }
}
// Löscht eine Lobby mit den angegebenen Daten aus der Lobbyliste.
function removeLobbyFromBrowser(lobbyData) {
    let row = lobbyBrowser.querySelector(`#entry-${lobbyData.lobbyId}`);
    if (row)
        row.remove();

    lobbyCounter.innerText = lobbyBrowser.children.length;
}
// Aktualisiert eine Lobby mit den angegebenen Daten.
function updateLobbyInBrowser(lobbyData) {
    let row = lobbyBrowser.querySelector(`#entry-${lobbyData.lobbyId}`);
    if (row)
        row.querySelector('.player-num-text').innerText = `${lobbyData.players} / ${lobbyData.maxPlayers}`;
}
// Löscht alle Einträge aus der Lobbyliste.
function clearLobbyBrowser() {
    while (lobbyBrowser.firstChild)
        lobbyBrowser.firstChild.remove();

    lobbyCounter.innerText = lobbyBrowser.children.length;
}
