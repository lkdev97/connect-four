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
let chatMessagesContainer;
let chatInputBox;
let chatSendButton;
let gameStatusContainer;
let gameStatusText;
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

/**
 * Sobald man einem bestehenden Spiel beitritt oder eins erstellt wird das Spielfeld angezeigt
 * 
 */
function showBoard(gameId) {
    board.classList.add('is--hidden');
    board.parentElement.classList.add('is--hidden');
    document.getElementById('game-status-container').classList.remove('is--hidden');
    document.getElementById('board').classList.remove('is--hidden');
    document.getElementById('chat-container').classList.remove('is--hidden');
    document.getElementById('join-game-wrapper').classList.add('is--hidden');
    document.getElementById('browse-lobbies-wrapper').classList.add('is--hidden');
    document.getElementById("leave-lobby").classList.remove('is--hidden');
    document.getElementById("url-box").classList.remove('is--hidden');
    document.getElementById("username").classList.add('is--hidden');

    document.getElementById("game-url").innerHTML = `
                                                    Viel Erfolg, ${playerName}!<br />
                                                    Spiel-Code: ${gameId}<br />
                                                    Link zum Spiel: <i>${location.href}</i>
                                                    `;
}

/**
 * Sobald man auf den Button "Leave Lobby" klickt, wird das Spielfeld ausgeblendet und man landet wieder auf der Startseite
 */
function hideBoard() {
    board.classList.remove('is--hidden');
    board.parentElement.classList.remove('is--hidden');
    document.getElementById('game-status-container').classList.add('is--hidden');
    document.getElementById('board').classList.add('is--hidden');
    document.getElementById('chat-container').classList.add('is--hidden');
    document.getElementById('join-game-wrapper').classList.remove('is--hidden');
    document.getElementById('browse-lobbies-wrapper').classList.remove('is--hidden');
    document.getElementById("leave-lobby").classList.add('is--hidden');
    document.getElementById("url-box").classList.add('is--hidden');
    document.getElementById("username").classList.remove('is--hidden');
}


// Lädt alle UI-Referenzen und registriert UI-Events.
// Gibt true zurück, falls alle UI-Elemente gefunden wurden.
function initUI() {
    if (updateUIReferences()) {
        createGameButton.addEventListener('click', createNewGame);
        txtUserName.addEventListener('click', changeUserName);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        joinGameId.addEventListener('keydown', (ev) => {
            if (ev.keyCode == 13) { // Enter Taste gedrückt
                ev.preventDefault();
                joinGameButton.click();
            }
        });
        leaveGameButton.addEventListener('click', disconnectFromGame);
        window.addEventListener('hashchange', () => {
            if (window.location.hash.length > 1) {
                joinGameId.value = window.location.hash.substr(1);

                if (!isConnectedToGame())
                    joinGame(joinGameId.value);
            }
        });
        chatSendButton.addEventListener('click', () => {
            sendChatMessage(chatInputBox.value);
            clearChatInput();
        });
        chatInputBox.addEventListener('keydown', (ev) => {
            if (ev.keyCode == 13) { // Enter Taste gedrückt
                ev.preventDefault();
                chatSendButton.click();
            }
        });

        return true;
    }

    return false;
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
    chatMessagesContainer = document.getElementById('chat-messages-container');
    chatInputBox = document.getElementById('chat-input-box');
    chatSendButton = document.getElementById('chat-send-button');
    gameStatusContainer = document.getElementById('game-status-container');
    gameStatusText = document.getElementById('game-status-text');

    return createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && leaveGameButton && lobbyBrowser && lobbyCounter && board && chatMessagesContainer && chatInputBox && chatSendButton && gameStatusContainer && gameStatusText;
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
        playerNumTextElement.innerText = `${lobbyData.players} / ${lobbyData.maxPlayers}${lobbyData.spectators > 0 ? ` (+${lobbyData.spectators})` : ''}`;
        // erstellt den Link, mit dem man sich zum Spiel verbinden kann
        let joinTextElement = document.createElement('td');
        let joinLinkElement = document.createElement('span');
        joinLinkElement.classList.add('link', 'join-link');
        joinLinkElement.innerText = `>> ${lobbyData.players < lobbyData.maxPlayers ? 'Beitreten' : 'Zuschauen'}`;
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
    if (row) {
        row.querySelector('.player-num-text').innerText = `${lobbyData.players} / ${lobbyData.maxPlayers}${lobbyData.spectators > 0 ? ` (+${lobbyData.spectators})` : ''}`;
        row.querySelector('.join-link').innerText = `>> ${lobbyData.players < lobbyData.maxPlayers ? 'Beitreten' : 'Zuschauen'}`;
    }
}
// Löscht alle Einträge aus der Lobbyliste.
function clearLobbyBrowser() {
    while (lobbyBrowser.firstChild)
        lobbyBrowser.firstChild.remove();

    lobbyCounter.innerText = lobbyBrowser.children.length;
}

// Fügt eine Chat-Nachricht hinzu.
function addChatMessage(message) {
    let messageElement = document.createElement('div');
    messageElement.classList.add('message');

    if (message.type)
        messageElement.classList.add(`message-${message.type}`);

    if (message.sender) {
        let senderText = document.createElement('span');
        senderText.classList.add('sender');
        senderText.textContent = message.sender;

        if (message.content)
            senderText.textContent += ':';

        messageElement.appendChild(senderText);
    }

    if (message.content) {
        let contentText = document.createElement('span');
        contentText.classList.add('content');
        contentText.textContent = message.content;
        messageElement.appendChild(contentText);
    }

    chatMessagesContainer.appendChild(messageElement);
    // nach unten scrollen
    chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight - chatMessagesContainer.clientHeight;
}
// Löscht alle Nachrichten aus dem Chat.
function clearChat() {
    while (chatMessagesContainer.firstChild)
        chatMessagesContainer.firstChild.remove();
}
// Löscht den Inhalt des Chat-Eingabefeldes.
function clearChatInput() {
    chatInputBox.value = '';
}

// Setzt den Text über dem Feld.
// Wird verwendet, um den aktuellen Spielernamen anzuzeigen, welcher gerade am Zug ist.
function setGameStatus(text) {
    gameStatusText.innerText = text;
}
