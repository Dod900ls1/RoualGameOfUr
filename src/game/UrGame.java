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

    /**
     * Creates {@code Player} instances as described by received {@code PlayerOptions} records and assigns t {@link #players}
     * @param playerOptions Collection of configuration records of players to be created
     */
    private void setupPlayers(PlayerOptions[] playerOptions) {
        this.players = new Player[playerOptions.length];
        for (int i = 0; i < playerOptions.length; i++) {
            players[i] = Player.createPlayerFromSetup(playerOptions[i], board.getPlayerPath(playerOptions[i].playerColour()), this);
            System.out.println(playerOptions[i]);
        }
    }

    /**
     * Returns collection of {@code Player} instances for game
     * @return {@link #players} array
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Returns the {@code Board} at current state for game
     * @return {@link #board} object
     */
    public Board getBoard() {
        return this.board;
    }
}