package controller;

import board.Tile;
import controller.action.game.MoveMade;
import controller.action.game.MoveSelected;
import controller.action.game.NoMovePossible;
import controller.action.game.RollDice;
import game.UrGame;
import player.Piece;
import player.Player;
import player.PlayerOptions;
import states.GameState;
import ui.GameInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.stream.Collectors;

public class GameController implements ActionListener {

    /**
     * Flag indicating if turn is currently in progress,
     * State monitored in {@link #beginGame()} and used to control game's turn cycle
     * Allows turns to be ended by different threads than those on which they are processed
     */
    volatile private Boolean turnInProgress;

    /**
     * Boolean indicator that game is still in play i.e. conclusion of current turn is not end of game
     */
    volatile private boolean play;
    private GameInterface gameInterface;

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
     * Creates new {@code GameInterface} as view component for this controller
     * @param playerOptions {@code PlayerOptions} collection of {@code Player} setup parameters to create new players from.
     */
    public void createGame(PlayerOptions[] playerOptions){
        this.game = new UrGame(playerOptions);
        initialiseGameEntityControllers();
        this.gameInterface = new GameInterface(this);
        Thread gameThread = new Thread( () -> this.beginGame());
        gameThread.start();
    }

    /**
     * Begins game by calling {@link PlayerController#startTurn()} on {@code activePlayerController}
     * Sets of turn loop calling {@link PlayerController#startTurn()} on {@code activePlayerController} when previous turn ends as indicated by state of {@code turnInProgress}
     */
    public synchronized int beginGame(){
        int turnCount=0;
        play=true;
        turnInProgress = false;
        while (play){
            try {
                if (turnInProgress){
                    synchronized (this){
                        while (turnInProgress){
                            wait();
                        }
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameInterface.resetForNewTurn(activePlayerController.requiresUserInput);
            //turnInProgress = true;
            turnCount++;
            activePlayerController.startTurn();
            turnInProgress=true;
        }

        return turnCount;
    }



    /**
     * Creates controllers for game entities for new {@code UrGame}
     * Controllers for board and tile instantiate their own model and view components
     */
    private void initialiseGameEntityControllers(){
        this.boardController=new BoardController(game.getBoard(), this);
        this.playerControllers = new ArrayList<>();
        for (Player player:game.getPlayers()) {
            playerControllers.add(PlayerController.getControllerForPlayer(player, this));
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
        } else if (e instanceof RollDice) {
            executeRoll((RollDice) e);
        } else if (e instanceof MoveMade) {
            finishMove((Piece) e.getSource());
        } else if (e instanceof NoMovePossible) {
            finishMove(null);
        }
    }


    private void executeRoll(RollDice e){
        int rollResult = activePlayerController.rollDice();
        gameInterface.showRollResult(rollResult);
        gameInterface.disableRoll();
        boolean canMove = rollResult!=0;
        if (canMove) {
            List<Tile> validTiles = activePlayerController.getValidTilesForMove();
            if (validTiles.isEmpty()) {
                canMove = false;
            } else {
                boardController.enableValidMoveTiles(validTiles);
            }
        }

        if (!canMove){
            gameInterface.showNoMovesMessage();
            finishMove(null);
        }
    }



    /**
     * Called at end of a player's turn.
     * Updates board to reflect moved piece.
     * Switches active player
     * @param pieceMoved {@code Piece} object moved last turn
     */
    public synchronized void finishMove(Piece pieceMoved){
        if (pieceMoved!=null) {
            this.boardController.updateBoard(pieceMoved);
        }
        play = activePlayerController.endTurn();
        activePlayerController = playerControllerIterator.next();
        turnInProgress =false;
        notifyAll();
        //activePlayerController.startTurn();
    }


    /**
     * Retrieves {@code TileController} instances controlling the {@code Tile} objects in {@code tiles} from {@link #boardController}
     * @param tiles {@code Tile} instances to get {@code TileController} for
     * @return Collection of {@code TileController} controllers for {@code tiles}
     */
    public List<TileController> getControllersForTiles(Collection<Tile> tiles) {
        Set<TileController> tileControllers = boardController.tileControllers;
        return tileControllers.stream().filter(tileController -> tiles.contains(tileController.getTile())).collect(Collectors.toList());

    }


    /**
     * Returns new {@code GameState} object to encapsulate current state of game at time of method call
     * @return new {@code GameState} representing state as known by {@code GameController}
     */
    public GameState getGameState() {
        GameState gameState =  game.bundle();
        gameState.activePlayerController = this.activePlayerController;
        return gameState;
    }

    /**
     * Accessor for {@code boardController} for ths game's board
     * @return {@link #boardController} for this game
     */
    public BoardController getBoardController() {
        return boardController;
    }


}
