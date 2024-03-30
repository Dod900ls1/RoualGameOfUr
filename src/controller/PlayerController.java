package controller;

import board.Tile;
import controller.action.game.MoveMade;
import controller.action.game.NoMovePossible;
import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;
import player.PlayerAI;
import player.PlayerHuman;

import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controller for {@code Player} objects. Links {@code Player} model entity to view
 */
public abstract class PlayerController implements ActionListener {

    /**
     * {@code Player} model for this controller
     */
    private final Player player;
    public boolean requiresUserInput;

    /**
     * Value of the player's last roll.
     * Stored here as dice rolls separate from piece movement in turn sequence
     * May move to inside {@link Player}?
     */
    int lastRoll;

    /**
     * {@code PlayerController} reports to {@link GameController}.
     * If event needs to be responded to from whole game scale, it is reported to {@code GameController}
     */
    final GameController parentListener;


    /**
     * Returns a new controller for the specified {@code player}
     * @param player Player to create controller for
     * parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     * @return {@code PlayerController} specialisation gven the type of {@code Player}
     */
    public static PlayerController getControllerForPlayer(Player player, GameController parentListener){
        if (player instanceof PlayerHuman){
            return new PlayerHumanController((PlayerHuman) player, parentListener);
        } else if (player instanceof PlayerAI) {
            return new PlayerAIController((PlayerAI) player, parentListener);

        }
        return null;
    }


    /**
     * Constructor for new {@code PlayerController}
     * Has access to {@link Player} instance - bridges communication between Player view and model
     * @param player {@code Player} model entity
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public PlayerController(Player player, GameController parentListener) {
        this.player = player;
        this.parentListener = parentListener;
    }


    /**
     * Starts a new thread to execute move in {@code Player} model.
     * Receives {@code Tile} instance selected to move to.
     * Calls {@link Player#makeMove(int, Tile) Player.makeMove} to execute move (checks move is valid and if so, moves the appropriate {@code Piece})
     * When move made, calls {@code fireMoveMade} to inform {@code GameController} that move has been executed
     * @param toMoveTo {@code Tile} to move {@code Piece} to
     */
    void makeMove(Tile toMoveTo){
        if (toMoveTo==null) {
            fireNoMovePossible();

        }else{
            try {
                Piece movedPiece = player.makeMove(lastRoll, toMoveTo);
                //System.out.printf("Player %d: Moved piece from tile %d to %d%n", player.getPlayerColour(), movedPiece.getLastTile().getTileNum(), movedPiece.getTile().getTileNum());
                fireMoveMade(movedPiece);
            } catch (IllegalMoveException e) {
                e.printStackTrace(); //Shouldn't happen if we disable buttons not in valid move set for this turn so cannot fire event!
            }
        }



    }

    /**
     * Rolls dice using {@link Player#rollDice()}.
     * Sets the dice value as {@code lastRoll}
     * May need to fire new event to {@link GameController} to provide roll value to view??
     * @return Rolled value
     */
     int rollDice(){
        lastRoll = player.rollDice();
        return lastRoll;
    }

    /**
     * Fires {@code MoveMade} event with source as {@code movedPiece} to {@code parentListener} who is {@link GameController}
     * @param movedPiece {@code Piece} object moved
     */
    private void fireMoveMade(Piece movedPiece){
       parentListener.actionPerformed(new MoveMade(movedPiece, movedPiece.getTileNumber()));
    }

    /**
     * Fires {@code NoMovePossible} event with source as {@code player} to {@code parentListener} who is {@link GameController}
     */
    private void fireNoMovePossible(){
        parentListener.actionPerformed(new NoMovePossible(player));
    }

    /**
     * Find {@code Tile} objects that player can move to in this turn (for current value of {@code lastRoll})
     * @return List of {@code Tile} instances that are valid, potential move options
     */
    public List<Tile> getValidTilesForMove(){
        return player.findPotentialMoves(lastRoll);
    }


    /**
     * Find {@code TileController} for {@code Tile} objects that player can move to in this turn (for current value of {@code lastRoll})
     * @return List of {@code TileController} for {@code Tile} instances that are valid, potential move options
     */
    public List<TileController> getValidTileControllersForMove(){
        return parentListener.getControllersForTiles(getValidTilesForMove());
    }

    /**
     * Gets number of {@code Piece} objects for {@code Player} that are on the board (occupying a {@code Tile} that is not Player's {@code PreStartTile} or {@code PostEndTile})
     * @return Number of pieces on board for player
     */
    public int getPlayerPieceOnBoardCount(){
        return player.getPieceOnBoardCount();
    }

    /**
     * Gets number of {@code Piece} objects for {@code Player} that are pre-board (occupying player's {@code PreStartTile} tile)
     * @return Number of pieces pre-board for player
     */
    public int getPlayerPiecePreBoardCount(){
        return player.getPiecePreBoardCount();
    }



    /**
     * Gets number of {@code Piece} objects for {@code Player} that are post-board (occupying {@code PostEndTile} tile)
     * @return Number of pieces post-board for player
     */
    public int getPiecePostBoardCount() {
        return player.getPiecePostBoardCount();
    }


    /**
     * Called at end of player's turn
     */
    public boolean endTurn() {
//        if (player.getPieceOnBoardCount()==0 && player.getPiecePreBoardCount()==0){
//            System.out.println("Game done");
//            return false;
//        }
//        return true;
        return player.hasPiecesLeft();
    }

    /**
     * Called at start of player's turn
     */
    public void startTurn() {

    }
}
