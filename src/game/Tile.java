package game;

import java.util.ArrayList;

public class Tile {
    private boolean isRosette;
    private ArrayList<Integer> adjoining = new ArrayList<>(4);
    private int tileNum;

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

    public boolean checkRelation(int relation) {
        if (relation < 1 || relation == 13 || relation == 15 || relation == 16 || relation == 18) {
            return false;
        }
        return true;
    }

    public boolean getRosette() {
        return this.isRosette;
    }

    public ArrayList getAdjoining() {
        return this.adjoining;
    }

}