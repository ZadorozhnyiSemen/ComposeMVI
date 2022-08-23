# Game lobby application

Demonstration of MVI library usage

Screen simulates creation of an online game lobby.
You can Start lobby by pressing "Create lobby" button.

After that, players will fill the lobby in short period of time. 
You, as a host, can kick any player except yourself.
When lobby is full you can start a game.

While having pretty easy [contract](presentation/GameLobbyContract.kt)
all the UI updates are handled by reducer function in [MVI](presentation/GameLobbyMviProcessor.kt)
