document.addEventListener('DOMContentLoaded', () => {
    let createGameButton = document.getElementById('create-game-button');
    let joinGameButton = document.getElementById('join-game-button');
    let joinGameId = document.getElementById('join-game-code');


    if (createGameButton && joinGameButton && joinGameId) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        console.log('All UI elements registered.');
    }



    function createNewGame() {
        console.log('Creating new game...');
        sendToServer('/create', {}).then((response) => {
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


    function sendToServer(target, data = {}) {
        return fetch(target, { body: JSON.stringify(data), method: "POST" }).then(response => response.json()).catch(console.error);
    }
});