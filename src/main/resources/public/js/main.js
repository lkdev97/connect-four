document.addEventListener('DOMContentLoaded', () => {
    let createGameButton = document.getElementById('create-game-button');
    let createGameIsPublicBox = document.getElementById('create-game-public-box');
    let joinGameButton = document.getElementById('join-game-button');
    let joinGameId = document.getElementById('join-game-code');
    let lobbyBrowser = document.getElementById('lobby-browser');


    if (createGameButton && createGameIsPublicBox && joinGameButton && joinGameId && lobbyBrowser) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        lobbyBrowser = lobbyBrowser.querySelector('tbody');
        console.log('All UI elements registered.');

        fetchGameList();
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


    function fetchGameList() {
        while (lobbyBrowser.firstChild)
            lobbyBrowser.firstChild.remove();

        sendToServer('/games', {})
            .then((response) => {
                console.log(response);
            });
    }


    function sendToServer(target, data = {}) {
        return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
    }
});