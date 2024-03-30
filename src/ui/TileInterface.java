package ui;

import controller.TileController;
import player.Player;

import javax.swing.*;
import java.util.Map;

public class TileInterface extends JButton{
    final TileController controller;
    boolean isRosette;

    boolean isNonWalkable;

    public TileInterface(TileController controller) {
        this.controller = controller;
        isRosette = controller.isRosette();
        isNonWalkable = controller.isNonWalkable();
        setButtonView();
    }

    private void setButtonView(){
        String text = "";
        if (isRosette){
            text="✯";
        } else if (isNonWalkable) {
            //DEBUGGING VIEW
            text= Integer.toString(controller.getTileNumber());
            //setVisible(false); //Button only there logically to not upset grid layout
        }else{
            text= Integer.toString(controller.getTileNumber());
        }
        setText(text);
        addActionListener(controller);
    }

    public void enableTile() {
        setEnabled(true);
    }

    public void updateTile() {

        //TODO Make prettier?

        Map<Player, Integer> piecesByPlayer = controller.getPiecesByPlayer();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Player, Integer> playerPiecesEntry : piecesByPlayer.entrySet()) {
            Integer playerColour = playerPiecesEntry.getKey().getPlayerColour();
            Integer pieceCount = playerPiecesEntry.getValue();
            sb.append(String.format("Player %d: %d%n", playerColour, pieceCount));
        }
        if (sb.isEmpty()){
            sb.append(controller.getTileNumber());
        }
        this.setText(sb.toString());
        this.repaint();
    }
}
