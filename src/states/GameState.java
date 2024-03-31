package states;

import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private BoardState boardState;

    List<PlayerState> playerStates;

   Integer activePlayer;

    /**
     * Sets player state information for this game state
     * @param playerStates List of {@code PlayerState} instances for each {@code Player}
     * @param activePlayer Colour value of active player (or value congruent to active player colour modulo {@code playerStates.size()})
     */
    public void setPlayerStates(List<PlayerState> playerStates, int activePlayer){
        this.playerStates = playerStates;
        this.activePlayer = activePlayer;
    }

    public void incrementActivePlayer(){
        //THE MODULO AND INCREMENT OPERATIONS ARE INTENTIONALLY SWAPPED HERE. THIS IS BECAUSE PLAYER COLOUR NUMBERS START AT 1 NOT 0!
        this.activePlayer%=playerStates.size();
        this.activePlayer++;
    }


    public void evolve(TileState[] potentialMove) {
        TileState[] potentialMoveInThisState = new TileState[]{ //translates potential move in terms of tile state instances in parent game state to tile state instances in this game state
                boardState.getTileStateByTileNumber(potentialMove[0].getTileNum()),
                boardState.getTileStateByTileNumber(potentialMove[1].getTileNum()),
        };
        boardState.evolve(potentialMoveInThisState, activePlayer);

    }


    public GameState copyState(){
        GameState copyState = new GameState();
        copyState.setPlayerStates(copyPlayerStates(), activePlayer);
        copyState.setBoardState(boardState.copyState()); //adds board state copy to game state copy using player state copies
        return copyState;
    }




    /**
     * Performs deep copy on {@link #playerStates} by calling {@link PlayerState#copyState()} for each player state
     * @return Deep copy of {@code playerStates}
     */
    private List<PlayerState> copyPlayerStates(){
        return playerStates.stream().map(p->p.copyState()).collect(Collectors.toList());
    }


    public List<PlayerState> getPlayerStates() {
        return playerStates;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public List<TileState[]> getMovesForPlayerForState(int roll) {
        return this.boardState.getMovesForPlayerForState(activePlayer, roll);
    }

    public int getActivePlayer() {
        return this.activePlayer;
    }

    public double getPiecesPostBoardForPlayer(int player) {
        return boardState.getPiecesPostBoardForPlayer(player);
    }
}
