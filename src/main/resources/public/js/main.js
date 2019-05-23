// Startpunkt
document.addEventListener('DOMContentLoaded', () => {
    // Falls alle UI Elemente gefunden wurden
    if (updateUIReferences()) {
        createGameButton.addEventListener('click', createNewGame);
        joinGameButton.addEventListener('click', () => joinGame(joinGameId.value));
        console.log('Alle UI Elemente gefunden und registriert.');

        fetchGameList();
        connectToGameListEvents();
    }
    else
        console.error('Manche UI Elemente fehlen.');
});
