document.addEventListener('DOMContentLoaded', () => {
    let createGameButton = document.getElementById('create-game-button');
    let createGameIsPublicBox = document.getElementById('create-game-public-box');
    let joinGameButton = document.getElementById('join-game-button');
    let joinGameId = document.getElementById('join-game-code');
    let lobbyBrowser = document.getElementById('lobby-browser');

    let lobbyBrowserEvents = new EventSource('/games');


    if (createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && lobbyBrowser) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        lobbyBrowser = lobbyBrowser.querySelector('tbody');
        console.log('All UI elements registered.');

        fetchGameList();
        setupGameListEvents();
    }



    function createNewGame() {
        console.log('Creating new game...');
        sendToServer('/create', { isPublic: createGameIsPublicBox.checked }).then((response) => {
            if (response.message)
                alert(response.message);
        });
    }
    function joinGame(gameId) {
        if (gameId) {
            console.log(`Joining game with id "${gameId}"...`);
            sendToServer('/join', { gameId }).then((response) => {
                if (response.message)
                    alert(response.message);
            });
        }
    }


    function setupGameListEvents() {
        lobbyBrowserEvents.addEventListener('addgame', (event) => addGameToBrowser(event.data));
        lobbyBrowserEvents.addEventListener('rmgame', (event) => removeGameFromBrowser(event.data));
    }

    function fetchGameList() {
        while (lobbyBrowser.firstChild)
            lobbyBrowser.firstChild.remove();

        sendToServer('/games', {})
            .then((response) => {
                if (response.games) {
                    for (let gameId of response.games)
                        addGameToBrowser(gameId);
                }
            });
    }
    function addGameToBrowser(gameId) {
        if (!lobbyBrowser.querySelector(`#entry-${gameId}`)) {
            let rowElement = document.createElement('tr');
            rowElement.id = gameId;

            let idTextElement = document.createElement('td');
            idTextElement.innerText = gameId;
            let playerNumTextElement = document.createElement('td');
            playerNumTextElement.innerText = '0';
            let joinTextElement = document.createElement('td');
            let joinLinkElement = document.createElement('span');
            joinLinkElement.className = 'link';
            joinLinkElement.innerText = '>> Beitreten';
            joinLinkElement.addEventListener('click', () => joinGame(gameId));
            joinTextElement.appendChild(joinLinkElement);

            rowElement.appendChild(idTextElement);
            rowElement.appendChild(playerNumTextElement);
            rowElement.appendChild(joinTextElement);

            lobbyBrowser.insertBefore(rowElement, lobbyBrowser.firstChild);
        }
    }
    function removeGameFromBrowser(gameId) {
        let row = lobbyBrowser.querySelector(`#entry-${gameId}`);
        if (row)
            row.remove();
    }


    function sendToServer(target, data = {}) {
        return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
    }
});