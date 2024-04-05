package ui;

import board.Tile;
import controller.TileController;
import player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class TileInterface extends JButton {

    final static ImageIcon[] pieceIcons;

    static {
        pieceIcons = new ImageIcon[3];
        String iconPath = "src/ui/pieces/";
        for (int i = 0; i < 3; i++) {
            pieceIcons[i] = getIconFromFile(iconPath+ i +".png");
        }
    }

    final TileController controller;
    // 1,2, or 3 : 0 if rosette or out
    private static int[] flowerTypes = { 0, 1, 0, 1, 2, 1, 2, 3, 2, 3, 0, 3, 0, 1, 0, 0, 2, 0, 0, 3, 0, 3, 1, 3 };
    private final boolean isPostBoard;
    private final boolean isPreBoard;
    boolean isRosette;

    boolean isNonWalkable;

    final ImageIcon[] flowerIcons; //0 is disabled, 1 is enabled

    static ImageIcon getIconFromFile(String filePath){
        try {
            return new ImageIcon(ImageIO.read(new File(filePath)).getScaledInstance(70, 70, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public TileInterface(TileController controller) {
        this.controller = controller;
        isRosette = controller.isRosette();
        isNonWalkable = controller.isNonWalkable();
        isPreBoard = controller.isPreBoard();
        isPostBoard = controller.isPostBoard();
        String enabledIconPath = "src/ui/flowers/";
        String disabledIconPath;
        if (isNonWalkable) {
            enabledIconPath += "disabled.png";
            disabledIconPath = enabledIconPath;


        } else {
            if (isRosette) {
                enabledIconPath += "flower0";
            } else if (controller.getTileType() == Tile.LIGHT_INDICATOR) {
                enabledIconPath += "flower1";
            } else if (controller.getTileType() == Tile.DARK_INDICATOR) {
                enabledIconPath += "flower2";
            } else {
                enabledIconPath += "flower3";
            }
            disabledIconPath = enabledIconPath + "-disabled.png";
            enabledIconPath +=".png";

        }

        flowerIcons = new ImageIcon[2];

        flowerIcons[0] = getIconFromFile(disabledIconPath);
        flowerIcons[1] = getIconFromFile(enabledIconPath);

        setButtonView();
    }


    private void setButtonView() {
        // String text = "";
//        String location = "src/ui/flowers/flower" + flowerTypes[controller.getTileNumber() - 1] + ".png";
//        String disabledLocation = "src/ui/flowers/flower" + flowerTypes[controller.getTileNumber() - 1]
//                + "-disabled.png";
//
//        if (isNonWalkable) {
//            // DEBUGGING VIEW
//            setText(Integer.toString(controller.getTileNumber()));
//            setVisible(false); // Button only there logically to not upset grid layout
//        }
//        setIcon(new ImageIcon(new ImageIcon(location).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
//        setDisabledIcon(new ImageIcon(
//                new ImageIcon(disabledLocation).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
//
        setDisabledIcon(flowerIcons[0]);
        setIcon(flowerIcons[1]);
        if (isPreBoard||isPostBoard){
            this.setText(String.format("%s:\n%d", (isPreBoard?"PREBOARD":"POSTBOARD"), controller.getPieceCount()));
            this.setFont(new Font("Arial", Font.PLAIN, 8));
            this.setForeground(Color.white);
            this.setHorizontalTextPosition(JButton.CENTER);
            this.setVerticalTextPosition(JButton.CENTER);
        }
        addActionListener(controller);
        setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
    }

    public void enableTile() {
        setEnabled(true);
    }

    public void updateTile() {



        if (!isNonWalkable) {
            Map<Player, Integer> piecesByPlayer = controller.getPiecesByPlayer();
            if (piecesByPlayer.keySet().size() > 1) {
                this.setDisabledIcon(pieceIcons[0]);
                this.setIcon(pieceIcons[0]);
            } else if (piecesByPlayer.keySet().size() > 0) {
                Optional<Player> occupier = piecesByPlayer.keySet().stream().findFirst();
                if (occupier.isPresent()) {
                    int occupierColour = occupier.get().getPlayerColour();
                    this.setIcon(pieceIcons[occupierColour]);
                    this.setDisabledIcon(pieceIcons[occupierColour]);
                }
            }else {
                this.setDisabledIcon(flowerIcons[0]);
                this.setIcon(flowerIcons[1]);
            }
        } else if (isPreBoard||isPostBoard){
            this.setText(String.format("%s:\n%d", (isPreBoard?"PREBOARD":"POSTBOARD"), controller.getPieceCount()));
        }


//        StringBuilder sb = new StringBuilder();
//
//        for (Map.Entry<Player, Integer> playerPiecesEntry : piecesByPlayer.entrySet()) {
//            Integer playerColour = playerPiecesEntry.getKey().getPlayerColour();
//            Integer pieceCount = playerPiecesEntry.getValue();
//
//            sb.append(String.format("src/ui/pieces/%d.png", playerColour));
//            this.setDisabledIcon(new ImageIcon(
//                    new ImageIcon(sb.toString()).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
//
//        }
//
//        if (sb.isEmpty()) {
//            this.setDisabledIcon(new ImageIcon(new ImageIcon(
//                    "src/ui/flowers/flower" + flowerTypes[controller.getTileNumber() - 1] + "-disabled.png").getImage()
//                    .getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
//            sb.append("src/ui/flowers/flower" + flowerTypes[controller.getTileNumber() - 1] + ".png");
//
//        }
//
//        this.setIcon(
//                new ImageIcon(new ImageIcon(sb.toString()).getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT)));
        this.repaint();
    }
}
