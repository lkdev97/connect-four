/* 
 * Beinhaltet den Startpunkt und sonstige wichtige Funktionen.
*/


let playerName;


// Startpunkt
document.addEventListener('DOMContentLoaded', () => {
    // Falls alle UI Elemente gefunden wurden
    if (initUI()) {
        createGameButton.addEventListener('click', createNewGame);
        txtUserName.addEventListener('click', changeUserName);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        console.log('Alle UI Elemente gefunden und registriert.');

        fetchGameList();
        connectToGameListEvents();

        setUserName(`Player${Math.floor(Math.random() * 9001 + 1000)}`);
        setUserName(prompt("Bitte gib deinen Spielernamen ein:", playerName) || playerName);

        if (location.hash.length > 0)
            joinGame(location.hash.substr(1));
    }
    else
        console.error('Manche UI Elemente fehlen.');
});
