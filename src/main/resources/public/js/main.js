/* 
 * Beinhaltet den Startpunkt und sonstige wichtige Funktionen.
*/


let playerName;


// Startpunkt
document.addEventListener('DOMContentLoaded', () => {
    // Falls alle UI Elemente gefunden wurden
    if (updateUIReferences()) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        console.log('Alle UI Elemente gefunden und registriert.');

        playerName = `Player${Math.floor(Math.random() * 9001 + 1000)}`;
        playerName = prompt("Bitte gib deinen Spielernamen ein:", playerName) || playerName;

        fetchGameList();
        connectToGameListEvents();
    }
    else
        console.error('Manche UI Elemente fehlen.');
});
