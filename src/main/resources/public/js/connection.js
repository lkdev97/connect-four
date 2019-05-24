/*
 * Beinhaltet alle Funktionen für die Kommunikation mit dem Server.
 */


// Speichert den WebSocket zum Verbinden mit dem Spiel.
let gameConnection = null;


// Fordert an, dass ein neues Spiel erstellt werden soll.
function createNewGame() {
    console.log('Erstelle ein neues Spiel...');
    sendToServer('/create', { isPublic: createGameIsPublicBox.checked }).then((response) => {
        if (response) {
            if (response.gameId)
                joinGame(response.gameId);
            else
                alert('Spiel konnte nicht erstellt werden.');
        }
    });
}
// Tritt einem Spiel unter der angegebenen ID bei.
function joinGame(gameId) {
    if (gameId) {
        // vom alten Spiel trennen
        disconnectFromGame();

        console.log(`Verbinde mit Spiel "${gameId}"...`);
        gameConnection = new WebSocket(`ws://${window.location.hostname}/${gameId}`);
        //gameConnection.addEventListener('open', () => showBoard(gameId));
        gameConnection.addEventListener('error', () => alert('Es ist ein Fehler bei der Übertragung aufgetreten.'));
        gameConnection.addEventListener('close', () => disconnectFromGame());
        gameConnection.addEventListener('message', ev => {
            let message = JSON.parse(ev.data);

            if (message) {
                if (message.gameField) {
                    setBoardContent(message.gameField);
                    showBoard(gameId);
                }
                else if (message.error)
                    alert(`Fehler: ${message.error}`);
                else
                    console.log(message);
            }
        });
    }
}
// Trennt die Verbindung mit dem aktuellen Spiel (falls verbunden) und versteckt das Spielfeld.
function disconnectFromGame() {
    if (isConnectedToGame()) {
        console.log('Verbindung mit Spiel getrennt.');
        gameConnection.close();
    }
    hideBoard();
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
                clearGameBrowser();

                // neue Einträge einfügen
                for (let gameId of response.games)
                    addGameToBrowser(gameId);
            }
        });
}

// Gibt einen boolean zurück, der aussagt, ob es eine Verbindung zum Spiel gibt.
function isConnectedToGame() {
    return gameConnection && gameConnection.readyState < WebSocket.CLOSING /* CONNECTING || OPEN */;
}


// Sendet Daten an den Server.
// Gibt ein Promise zurück, das "erfüllt" wird, wenn die Anfrage erfolgreich gestellt wurde und die Antwort
// ein gültiges JSON-Objekt ist.
function sendToServer(target, data = {}) {
    return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
}
