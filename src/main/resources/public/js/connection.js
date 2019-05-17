// Speichert später den WebSocket zum Verbinden mit dem Spiel (TODO)
let gameConnection = null;


// Fordert an, dass ein neues Spiel erstellt werden soll.
function createNewGame() {
    console.log('Erstelle ein neues Spiel...');
    sendToServer('/create', { isPublic: createGameIsPublicBox.checked }).then((response) => {
        if (response && response.message)
            alert(response.message);
    });
}
// Tritt einem Spiel unter der angegebenen ID bei.
function joinGame(gameId) {
    if (gameId) {
        // vom alten Spiel trennen
        disconnectFromGame();

        console.log(`Verbinde mit Spiel "${gameId}"...`);
        // TODO: mit /game via websocket verbinden
        sendToServer('/join', { gameId }).then((response) => {
            if (response && response.message)
                alert(response.message);
        });
    }
}
// Trennt die Verbindung mit dem aktuellen Spiel (falls verbunden).
function disconnectFromGame() {
    if (gameConnection && gameConnection.readyState < WebSocket.CLOSING /* CONNECTING || OPEN */) {
        console.log('Verbindung mit Spiel getrennt.');
        gameConnection.close();
    }
}


// Verbindet sich mit dem Server und hält per SSE (server-sent events) die Spieleliste aktuell.
function connectToGameListEvents() {
    let lobbyBrowserEvents = new EventSource('/gamelist');
    // Bei einem Fehler soll der EventSource client nach 15 Sekunden vesuchen, sich neu verbinden.
    // Da es sein kann, dass in diesen 15 Sekunden neue Spiele verpasst wurden, wird zusätzlich eine Anfrage zum Aktualisieren der Liste geschickt.
    lobbyBrowserEvents.addEventListener('error', () => {
        // aktuellen client schließen
        lobbyBrowserEvents.close();

        setTimeout(() => {
            fetchGameList();
            connectToGameListEvents();
        }, 15000);
    });

    // Events, die vom Server gesendet werden (addgame & rmgame)
    lobbyBrowserEvents.addEventListener('addgame', (event) => addGameToBrowser(event.data));
    lobbyBrowserEvents.addEventListener('rmgame', (event) => removeGameFromBrowser(event.data));
}
// Aktualisiert die gesamte Spieleliste (indem alle Einträge gelöscht werden und neue vom Server geholt werden).
function fetchGameList() {
    sendToServer('/gamelist', {})
        .then((response) => {
            if (response && response.games) {
                // alle vorhandenen Einträge löschen
                while (lobbyBrowser.firstChild)
                    lobbyBrowser.firstChild.remove();

                // neue Einträge einfügen
                for (let gameId of response.games)
                    addGameToBrowser(gameId);
            }
        });
}


// TODO: ------- move to ui.js -------
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
    }
}
// Löscht ein Spiel mit der angegebenen ID aus der Spieleliste.
function removeGameFromBrowser(gameId) {
    let row = lobbyBrowser.querySelector(`#entry-${gameId}`);
    if (row)
        row.remove();
}
// -------


// Sendet Daten an den Server.
// Gibt ein Promise zurück, das "erfüllt" wird, wenn die Anfrage erfolgreich gestellt wurde und die Antwort
// ein gültiges JSON-Objekt ist.
function sendToServer(target, data = {}) {
    return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
}




// Startpunkt
document.addEventListener('DOMContentLoaded', () => {
    updateUIReferences();

    // Falls alle UI Elemente gefunden wurden
    if (createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && lobbyBrowser) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        lobbyBrowser = lobbyBrowser.querySelector('tbody');
        console.log('Alle UI Elemente gefunden und registriert.');

        fetchGameList();
        connectToGameListEvents();
    }
    else
        console.error('Manche UI Elemente fehlen.');
});