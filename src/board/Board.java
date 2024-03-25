package board;

import player.Player;

import java.util.*;

public class Board {

    /**
     * Squares in board layout are one of 4 states, each represented with 2 bits.
     * --Non-navigable -> 00
     * --Light player safe -> 01
     * --Dark player safe -> 10
     * --Shared space --> 11
     *
     * Tile is either rosette (1) or not rosette (0)
     */
    static List<BoardLayout> boardLayouts;
    static {
        boardLayouts = new ArrayList<>();
        boardLayouts.add(
            new BoardLayout(
                Long.parseLong(
                    "111"+
                    "111"+
                    "111"+
                    "111"+
                    "010"+
                    "010"+
                    "010"+
                    "111"+
                    "111",
                    2), //navigable tiles
                new int[]{1, 3, 11, 15, 17}, //rosette tiles
                new int[]{10, 7, 4, 1, 2, 5, 8, 11, 14, 17, 20, 23, 22, 19}, //light path
                new int[]{12, 9, 6, 3, 2, 5, 8, 11, 14, 17, 20, 23, 24, 21} //dark path
            )
        );
    }

    private BoardLayout layout;

    public Collection<Tile> getTiles() {
        return this.tiles;
    }

    public record PlayerBoard(ArrayList<Tile> tilePath, Tile startTile){}

    int tileCount;
    Set<Tile> tiles;

    public Board(){
        tileCount=0;
        generateTiles(0);
    }

    private void generateTiles(int layoutNumber) {
        this.layout = boardLayouts.get(layoutNumber);
        Long tilePositions = Long.reverse(layout.tiles()); //board layout stored with msb at top left, easiest to access bits starting from lsb therefore reverse bits long
        while (tilePositions>0){
            addNextTile(layout);
            tilePositions >>= 1L;
        }
    }

    private void addNextTile(BoardLayout layout) {
        tileCount++; //increment even if tile not added
        boolean nextTileWalkable = (layout.tiles() & 1) == 1;
        if (nextTileWalkable){
            boolean isRosette=false;
            int tileType = 0;
            if (Arrays.stream(layout.rosettes()).anyMatch(t->t==tileCount)){
                isRosette=true;
            }

            if (Arrays.stream(layout.lightPath()).anyMatch(t->t==tileCount)){
                tileType+=Tile.LIGHT_INDICATOR;
            }
            if (Arrays.stream(layout.darkPath()).anyMatch(t->t==tileCount)){
                tileType+=Tile.DARK_INDICATOR;
            }

            if (isRosette){
                tiles.add(new Rosette(tileCount, tileType));
            }
            else{
                tiles.add(new Tile(tileCount, tileType));
            }

        }


    }


    public List<Tile> getPlayerPath(int playerColour) {
        int[] pathTileNumbers;
        List<Tile> pathTiles = new ArrayList<>();
        if (playerColour==Player.LIGHT_PLAYER){
            pathTileNumbers = layout.lightPath();
        }
        else{
            pathTileNumbers = layout.darkPath();
        }
        for (int tileNumber : pathTileNumbers) {
            pathTiles.add(tiles.stream().filter(t->t.getTileNum()==tileNumber).findFirst().orElse(null));
        }
        return pathTiles;
    }

}
