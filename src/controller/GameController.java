package controller;

import controller.action.game.MoveMade;
import controller.action.game.MoveSelected;
import controller.action.game.RollDiceAction;
import game.UrGame;
import player.Piece;
import player.Player;
import player.PlayerOptions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameController implements ActionListener {



    /**
     * Provides circular {@code Iterator} of {@code controllers}. Next {@link #activePlayerController} obtained by calling {@code next} on returned {@code Iterator}
     * @param controllers List of {@code PlayerController} instances to iterate over
     * @return Circular iterator of {@code PlayerController} objects
     */
    Iterator<PlayerController> getControllerIterator(List<PlayerController> controllers){
        return new Iterator<>() {
            int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return true; //circular so should always have next
            }

            @Override
            public PlayerController next() {
                return controllers.get(currentIndex++ % controllers.size());
            }
        };
    }


    /**
     * {@code BoardController} for {@code Board} object in {@code UrGame} instance
     */
    BoardController boardController;

    /**
     * {@code PlayerController} list for {@code Player} instances in {@code UrGame}
     */
    List<PlayerController> playerControllers;

    /**
     * Circular iterator for {@code playerControllers} list. Obtained from {@link #getControllerIterator(List) getControllerIterator}
     */
    Iterator<PlayerController> playerControllerIterator;

    /**
     * {@code PlayerController} for player who has current turn
     */
    PlayerController activePlayerController;

    /**
     * {@code UrGame} model for this controller
     */
    private UrGame game;

    /**
     * {@code GameController} reports to {@link MainController}.
     * If event needs to be responded to from whole system scale, it is reported to {@code MainController}
     */
    private final MainController parentListener;



    /**
     * Controller for {@code UrGame} model.
     * Contains all other controllers for game entities
     * @param parentListener Attached listener who is step above in command chain, can fire events to this listener who can respond from higher order or fire to their parent etc.
     */
    public GameController(MainController parentListener){
        this.parentListener = parentListener;

    }

    /**
     * Creates new {@code UrGame} as model component for ths controller
     * @param playerOptions {@code PlayerOptions} collection of {@code Player} setup parameters to create new players from.
     */
    public void createGame(PlayerOptions[] playerOptions){
        this.game = new UrGame(playerOptions);
        initialiseGameEntityControllers();
    }

    /**
     * Creates controllers for game entities for new {@code UrGame}
     */
    private void initialiseGameEntityControllers(){
        this.boardController=new BoardController(game.getBoard(), this);
        this.playerControllers = new ArrayList<>();
        for (Player player:game.getPlayers()) {
            playerControllers.add(new PlayerController(player, this));
        }
        this.playerControllerIterator = getControllerIterator(playerControllers);
        this.activePlayerController=playerControllerIterator.next();
    }




    /**
     * {@code ActionListener} override. Responds to events fired from game components and entity controllers that require game scope to respond to.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e instanceof MoveSelected){
            this.activePlayerController.actionPerformed(e);
        } else if (e instanceof RollDiceAction) {
            this.activePlayerController.actionPerformed(e);
        } else if (e instanceof MoveMade) {
            finishMove((Piece) e.getSource());
        }
    }

    /**
     * Called at end of a player's turn.
     * Updates board to reflect moved piece.
     * Switches active player
     * @param pieceMoved {@code Piece} object moved last turn
     */
    public void finishMove(Piece pieceMoved){
        this.boardController.updateBoard(pieceMoved);
        activePlayerController.endTurn();
        activePlayerController = playerControllerIterator.next();
        activePlayerController.startTurn();
    }


}
