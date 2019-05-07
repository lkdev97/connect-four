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
        alert('Creating new game...');
    }
    function joinGame(gameId) {
        if (gameId)
            alert(`Joining game with id "${gameId}"...`);
    }
});