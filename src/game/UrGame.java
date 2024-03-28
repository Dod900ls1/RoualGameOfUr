package game;


import board.Board;
import player.Player;
import player.PlayerOptions;

public class UrGame{

    Board board;
    Player[] players;

    public UrGame(PlayerOptions[] playerOptions) {
        board = new Board();
        setupPlayers(playerOptions);
    }

    private void setupPlayers(PlayerOptions[] playerOptions) {
        this.players = new Player[playerOptions.length];
        for (int i = 0; i < playerOptions.length; i++) {
            players[i] = Player.createPlayerFromSetup(playerOptions[i], board.getPlayerPath(playerOptions[i].playerColour()));
            System.out.println(playerOptions[i]);
        }
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Board getBoard() {
        return this.board;
    }
}