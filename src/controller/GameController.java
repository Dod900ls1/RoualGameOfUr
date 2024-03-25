package controller;

import controller.action.MoveMade;
import controller.action.MoveSelected;
import controller.action.RollDiceAction;
import game.UrGame;
import player.Piece;
import player.Player;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameController implements ActionListener {

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


    BoardController boardController;
    List<PlayerController> playerControllers;
    Iterator<PlayerController> playerControllerIterator;
    
    PlayerController activePlayerController;

    public GameController(UrGame game){
        this.boardController=new BoardController(game.getBoard(), this);
        this.playerControllers = new ArrayList<>();
        for (Player player:game.getPlayers()) {
            playerControllers.add(new PlayerController(player, this));
        }
        this.playerControllerIterator = getControllerIterator(playerControllers);
        this.activePlayerController=playerControllerIterator.next();
    }

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

    public void finishMove(Piece pieceMoved){
        this.boardController.updateBoard(pieceMoved);
        activePlayerController.endTurn();
        activePlayerController = playerControllerIterator.next();
        activePlayerController.startTurn();
    }


}
