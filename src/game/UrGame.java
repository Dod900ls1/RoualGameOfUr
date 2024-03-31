package game;


import board.Board;
import board.Tile;
import player.Player;
import player.PlayerOptions;
import states.GameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UrGame{

    Board board;
    List<Player> players;

    private Player activePlayer;

    public UrGame(PlayerOptions[] playerOptions) {
        board = new Board();
        setupPlayers(playerOptions);
    }

    public void setActivePlayer(Player player){
        this.activePlayer = player;
    }

    /**
     * Creates {@code Player} instances as described by received {@code PlayerOptions} records and assigns t {@link #players}
     * @param playerOptions Collection of configuration records of players to be created
     */
    private void setupPlayers(PlayerOptions[] playerOptions) {
        this.players = new ArrayList<>();
        for (int i = 0; i < playerOptions.length; i++) {
            players.add(Player.createPlayerFromSetup(playerOptions[i], board.getPlayerPath(playerOptions[i].playerColour()), this));
            System.out.println(playerOptions[i]);
        }
    }

    /**
     * Returns collection of {@code Player} instances for game
     * @return {@link #players} collection
     */
    public Collection<Player> getPlayers() {
        return this.players;
    }

    /**
     * Returns the {@code Board} at current state for game
     * @return {@link #board} object
     */
    public Board getBoard() {
        return this.board;
    }



    /**
     * Returns new {@code GameState} object to encapsulate current state of game at time of method call
     * @return new {@code GameState} representing state as known by {@code UrGame}
     */
    public GameState bundle(){
        GameState currentState = new GameState();
        currentState.setPlayerStates(
                players.stream().map(player -> player.bundle()).collect(Collectors.toList()),
                activePlayer.getPlayerColour()
        );
        currentState.setBoardState(board.bundle());
        return currentState;
    }

    public double[] getRollProbabilities() {
        return new double[]{0.0625, 0.25, 0.375, 0.25, 0.0625};
    }

    public Tile getTileFromNumber(int tileNumber) {
        return board.getTileFromNumber(tileNumber);
    }
}