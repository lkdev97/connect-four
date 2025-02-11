/*
 * Beinhaltet alle Funktionen für die Kommunikation mit dem Server.
 */


// Speichert den WebSocket zum Verbinden mit dem Spiel.
let gameConnection = null;
// Speichert den aktuellen "Ping-Timer"
// (ein Timer, der alle 20 Sekunden ein Ping-Packet an den Server sendet, um das automatische Schließen des WebSockets durch Inaktivität zu vermeiden)
let pingTimerHandle = null;


// Beinhaltet die IDs von Packets.
let Packet = {
    In: {
        ERROR: 0,
        CONNECT_SUCCESS: 1,

        GAME_STATE: 16,
        CHAT_MESSAGE: 17
    },
    Out: {
        PING: 0,
        CONNECT_REQUEST: 1,

        PLAYER_MOVE: 16,
        CHAT_MESSSAGE: 17
    }
};
// Verhindert, dass die Werte innerhalb von Packet verändert werden können
Object.freeze(Packet);
Object.freeze(Packet.In);
Object.freeze(Packet.Out);


// Fordert an, dass ein neues Spiel erstellt werden soll.
function createNewGame() {
    console.log('Erstelle ein neues Spiel...');
    sendToServer('/create', { isPublic: createGameIsPublicBox.checked }).then((response) => {
        if (response) {
            if (response.lobbyId)
                joinGame(response.lobbyId);
            else
                alert('Spiel konnte nicht erstellt werden.');
        }
    });
}
// Tritt einem Spiel unter der angegebenen ID bei.
function joinGame(lobbyId) {
    if (lobbyId) {
        // vom alten Spiel trennen
        disconnectFromGame();

        console.log(`Verbinde mit Spiel "${lobbyId}"...`);
        gameConnection = new WebSocket(`ws://${window.location.hostname}/${lobbyId}`);
        gameConnection.addEventListener('open', () => sendToGame(Packet.Out.CONNECT_REQUEST, { playerName }));
        gameConnection.addEventListener('error', () => alert('Es ist ein Fehler bei der Übertragung aufgetreten.'));
        gameConnection.addEventListener('close', () => disconnectFromGame());
        gameConnection.addEventListener('message', ev => {
            resetPingTimer();
            let message = JSON.parse(ev.data);

            if (message && message.packetId !== undefined && message.data) {
                switch (message.packetId) {
                    case Packet.In.ERROR:
                        alert(`Fehler: ${message.data.error}`);
                        break;

                    case Packet.In.CONNECT_SUCCESS:
                        playerName = message.data.playerName;
                        location.hash = message.data.lobbyId;
                        showBoard(message.data.lobbyId);
                        break;

                    case Packet.In.GAME_STATE:
                        setGameStatus(message.data.status);
                        setBoardContent(message.data.gameField);
                        break;

                    case Packet.In.CHAT_MESSAGE:
                        addChatMessage(message.data);
                        break;

                    default:
                        console.log(message);
                        break;
                }
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
    clearBoardContent();
    clearChat();
    setGameStatus('');
    location.hash = '';
}


// Verbindet sich mit dem Server und hält per SSE (server-sent events) die Spieleliste aktuell.
function connectToGameListEvents() {
    let lobbyBrowserEvents = new EventSource('/lobbylist');
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

    // Events, die vom Server gesendet werden (addlobby, rmlobby & updlobby)
    lobbyBrowserEvents.addEventListener('addlobby', (event) => addLobbyToBrowser(JSON.parse(event.data)));
    lobbyBrowserEvents.addEventListener('rmlobby', (event) => removeLobbyFromBrowser(JSON.parse(event.data)));
    lobbyBrowserEvents.addEventListener('updlobby', (event) => updateLobbyInBrowser(JSON.parse(event.data)));
}
// Aktualisiert die gesamte Lobbyliste (indem alle Einträge gelöscht werden und neue vom Server geholt werden).
function fetchGameList() {
    sendToServer('/lobbylist', {})
        .then((response) => {
            if (response && response.lobbies) {
                clearLobbyBrowser();

                // neue Einträge einfügen
                for (let lobbyId of response.lobbies)
                    addLobbyToBrowser(lobbyId);
            }
        });
}

// Stoppt den Ping-Timer.
function stopPingTimer() {
    if (pingTimerHandle !== null) {
        clearInterval(pingTimerHandle);
        pingTimerHandle = null;
    }
}
// Setzt den Ping-Timer zurück und startet ihn wieder bei 0 Sekunden.
function resetPingTimer() {
    stopPingTimer();
    pingTimerHandle = setInterval(sendPing, 20000);
}

// Gibt einen boolean zurück, der aussagt, ob es eine Verbindung zum Spiel gibt.
function isConnectedToGame() {
    return gameConnection && gameConnection.readyState < WebSocket.CLOSING /* CONNECTING || OPEN */;
}


// Sendet ein Ping-Packet an den Server.
function sendPing() {
    sendToGame(Packet.Out.PING);
}

// Sendet einen Spielzug an den Server.
// Der Spielzug besteht aus der Spaltennummer, in die der Spieler seinen Stein legt.
function sendMove(column) {
    sendToGame(Packet.Out.PLAYER_MOVE, { column });
}

// Sendet eine Chat-Nachricht an den Server.
function sendChatMessage(content) {
    sendToGame(Packet.Out.CHAT_MESSSAGE, { content });
}


// Sendet Daten an den Server.
// Gibt ein Promise zurück, das "erfüllt" wird, wenn die Anfrage erfolgreich gestellt wurde und die Antwort
// ein gültiges JSON-Objekt ist.
function sendToServer(target, data = {}) {
    return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
}

// Sendet Daten an das Spiel, mit welchem man aktuell verbunden ist.
// Die Daten sollten ein JSON-Objekt sein, das in ein Packet umgewandelt werden kann.
function sendToGame(packetId, data = {}) {
    if (data && isConnectedToGame())
        gameConnection.send(JSON.stringify({ packetId, data }));
}
