/* 
 * Beinhaltet den Startpunkt und sonstige wichtige Funktionen.
*/


let playerName;


// Startpunkt
document.addEventListener('DOMContentLoaded', () => {
    if (initUI()) {
        console.log('Alle UI Elemente gefunden und registriert.');

        fetchGameList();
        connectToGameListEvents();

        setUserName(`Player${Math.floor(Math.random() * 9001 + 1000)}`);
        setUserName(prompt("Bitte gib deinen Spielernamen ein:", playerName) || playerName);

        if (location.hash.length > 1)
            joinGame(location.hash.substr(1));
    }
    else
        console.error('Manche UI Elemente fehlen.');
});
