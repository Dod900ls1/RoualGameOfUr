package controller;

import board.Tile;
import controller.action.game.*;
import game.UrGame;
import player.*;
import server.ClientActionListener;
import server.ServerActionListener;
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
    private Object lastMoveStash;

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
                PlayerController nextController = controllers.get(currentIndex++ % controllers.size());
                game.setActivePlayer(nextController.getPlayer());
                return nextController;
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
            turnInProgress = true;
            turnCount++;
            activePlayerController.startTurn();
            //turnInProgress=true;
        }
        System.out.println("Player "+activePlayerController.getPlayer().getPlayerColour()+ " won");
        return turnCount;
    }


    /**
     * Called on Client {@link PlayerHumanController} to start turn in {@link PlayerRemoteController} to send message back to server
     */
    public void switchToPlayerRemote(){
        PlayerRemoteController remoteController = getRemotePlayerController();
        remoteController.startTurn(); //will send message back to server about game state which wll be picked up in endTurn() for client (who is RemotePlayer on server)

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

        stashLastMove();

        play = activePlayerController.endTurn();
        if(play) {
            activePlayerController = playerControllerIterator.next();
        }
        turnInProgress =false;
        notifyAll();
        //activePlayerController.startTurn();
    }

    /**
     * Generates a JSON string containing data about game to send to remote
     */
    private void stashLastMove() {
        lastMoveStash = "";
    }


    /**
     * Parse JSON string and update game/players on local with stash data from remote
     */
    public void updateFromStash(){
    }




    public Object getLastTurnInformation() {
        return lastMoveStash;
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
        return gameState;
    }

    /**
     * Accessor for {@code boardController} for ths game's board
     * @return {@link #boardController} for this game
     */
    public BoardController getBoardController() {
        return boardController;
    }


    public void createGameAsServer(GameStartedWithServer.GameStartedWithServerEventSource gameStartedWithServerEventSource) {
        PlayerOptions[] playerOptions = gameStartedWithServerEventSource.playerOptions();
        this.game = new UrGame(playerOptions);
        initialiseGameEntityControllersWithRemote(gameStartedWithServerEventSource.serverListener(), gameStartedWithServerEventSource.clientListener());
        this.gameInterface = new GameInterface(this);
        Thread gameThread = new Thread( () -> this.beginGame());
        gameThread.start();

    }

    /**
     * Called in client with message sent from {@link PlayerRemoteController#initialiseRemote()} from server.
     * Use data to create client's game.
     * @param gameSetupMessageFromServer
     */
    public void createGameAsClient(Object gameSetupMessageFromServer){
        //use READY_TO_START message received from server to create a new game, gameInterface
        this.game = new UrGame(); //PLayer options parsed for gameSetupMessageFromServer
        this.gameInterface= new GameInterface(this);
    }


    /**
     * Creates player controllers for local player as {@link PlayerHumanController} and remote as {@link PlayerRemoteController}
     * @param serverActionListener
     * @param clientActionListener
     */
    public void initialiseGameEntityControllersWithRemote(ServerActionListener serverActionListener, ClientActionListener clientActionListener){
        this.boardController=new BoardController(game.getBoard(), this);
        this.playerControllers = new ArrayList<>();
        PlayerRemote remotePlayer = (PlayerRemote) game.getPlayers().stream().filter(p->p instanceof PlayerRemote).findFirst().orElse(null);
        PlayerHuman localPlayer = (PlayerHuman) game.getPlayers().stream().filter(p->p instanceof PlayerHuman).findFirst().orElse(null);
        this.playerControllers.add(new PlayerRemoteController(remotePlayer, this, serverActionListener, clientActionListener));
        this.playerControllers.add(new PlayerHumanController(localPlayer, this));
        this.playerControllerIterator = getControllerIterator(playerControllers);
        this.activePlayerController=playerControllerIterator.next();
    }


    public PlayerRemoteController getRemotePlayerController()
    {
        return (PlayerRemoteController) playerControllers.stream().filter(pc -> pc instanceof PlayerRemoteController).findFirst().orElse(null);
    }

    /**
     * Called when {@link PlayerRemoteController} is intialised - contains configuration data needed to recreate game setup on remote
     * @return
     */
    public Object getRemoteInitMessage() {

    }
}
