package ui;

import controller.TileController;
import player.Player;

import javax.swing.*;
import java.util.Map;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;

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
        //String text = "";
        String location = "src/ui/flowers/";

        if (isRosette){
            //text="✯";
            location += "Rosette.png";
        } else if (isNonWalkable) {

            //DEBUGGING VIEW
            setText( Integer.toString(controller.getTileNumber()));
            setVisible(false); //Button only there logically to not upset grid layout
        }else{
            location += "Tile0.png";
            //text= Integer.toString(controller.getTileNumber());
        }
        //setText(text);
        setIcon(new ImageIcon(new ImageIcon(location).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        setDisabledIcon(new ImageIcon(new ImageIcon("src/ui/flowers/disabled.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        addActionListener(controller);
        setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
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

            sb.append(String.format("src/ui/pieces/%d.png", playerColour));
            this.setDisabledIcon(new ImageIcon(new ImageIcon(sb.toString()).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        }
        if (sb.isEmpty()){
            this.setDisabledIcon(new ImageIcon(new ImageIcon("src/ui/flowers/disabled.png").getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
            sb.append("src/ui/flowers/tile1.png");

        }
        this.setIcon(new ImageIcon(new ImageIcon(sb.toString()).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        this.repaint();
    }
}
