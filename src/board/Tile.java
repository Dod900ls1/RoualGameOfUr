package board;

import player.Piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Tile {
    public static final int LIGHT_INDICATOR=1;
    public static final int DARK_INDICATOR=2;
    public static final int SHARED_INDICATOR= LIGHT_INDICATOR+DARK_INDICATOR;
    private boolean isRosette;
    private ArrayList<Integer> adjoining = new ArrayList<>(4);
    private int tileNum;
    private int tileType;

    private Set<Piece> piecesOnTile;

    public Tile(int tileNum, int tileType){
        this.tileNum=tileNum;
        this.tileType = tileType;
        this.piecesOnTile=new HashSet<>();
    }
/**
    public Tile(int tileNum) {
        this.tileNum = tileNum;

        if (tileNum == 1 || tileNum == 3 || tileNum == 11 || tileNum == 15 || tileNum == 17) {
            this.isRosette = true;
        } else {
            this.isRosette = false;
        }

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    if (checkRelation((tileNum + 1))) {
                        adjoining.add(tileNum + 1);
                    } else {
                        adjoining.add(0);
                    }
                    break;
                case 1:
                    if (checkRelation((tileNum - 1))) {
                        adjoining.add(tileNum - 1);
                    } else {
                        adjoining.add(0);
                    }
                    break;
                case 2:
                    if (checkRelation((tileNum + 3))) {
                        adjoining.add(tileNum + 3);
                    } else {
                        adjoining.add(0);
                    }
                    break;
                case 3:
                    if (checkRelation((tileNum - 3))) {
                        adjoining.add(tileNum - 3);
                    } else {
                        adjoining.add(0);
                    }
                    break;
            }
        }
    }
*/
    public int getTileNum() {
        return this.tileNum;
    }

    public boolean checkRelation(int relation) {
        if (relation < 1 || relation == 13 || relation == 15 || relation == 16 || relation == 18) {
            return false;
        }
        return true;
    }

    public boolean getRosette() {
        return this.isRosette;
    }

    public ArrayList<Integer> getAdjoining() {
        return this.adjoining;
    }

    public void removePiece(Piece piece) {
        piecesOnTile.remove(piece);
    }

    public void addPiece(Piece piece){
        piecesOnTile.add(piece);
    }
}