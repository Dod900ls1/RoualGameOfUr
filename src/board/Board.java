package board;

import player.Player;

import java.util.*;

public class Board {

    /**
     * Board layouts stored. Potential to add more layouts if needed.
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

    /**
     * Layout for this {@code Board}
     */
    private BoardLayout layout;

    /**
     * Return {@code tiles} collection for ths {@code Board}
     * @return {@link #tiles} collection
     */
    public Collection<Tile> getTiles() {
        return this.tiles;
    }

    /**
     * All {@code Tile} objects in this board
     */
    Set<Tile> tiles;

    /**
     * Board constructor. Calls {@code generateTiles} to create board tiles
     */
    public Board(){
        generateTiles(0);
    }

    /**
     * Creates tiles as specified by the {@code BoardLayout} at index {@code layoutNumber} in {@link #boardLayouts}
     * @param layoutNumber Index of layout to be used in {@code boardLayouts}
     */
    private void generateTiles(int layoutNumber) {
        this.tiles = new HashSet<>();
        int tileCount = 1;
        this.layout = boardLayouts.get(layoutNumber);
        Long tilePositions = Long.reverse(layout.tiles()); //board layout stored with msb at top left, easiest to access bits starting from lsb therefore reverse bits long
        while (tilePositions>0){
            addNextTile(layout, tileCount);
            tilePositions >>= 1L;
            tileCount++; //increment count even if tile not added
        }
    }

    /**
     * Creates next tile in board as described by {@code layout} and adds to {@code tiles} collection
     * @param layout Layout used to create board
     * @param tileNumber Number of tile to create -- this may not be the number of last tile created as non-walkable bord spaces are still given numbers
     */
    private void addNextTile(BoardLayout layout, int tileNumber) {
        boolean nextTileWalkable = (layout.tiles() & 1) == 1;
        if (nextTileWalkable){
            boolean isRosette=false;
            int tileType = 0;
            if (Arrays.stream(layout.rosettes()).anyMatch(t->t==tileNumber)){
                isRosette=true;
            }

            if (Arrays.stream(layout.lightPath()).anyMatch(t->t==tileNumber)){
                tileType+=Tile.LIGHT_INDICATOR;
            }
            if (Arrays.stream(layout.darkPath()).anyMatch(t->t==tileNumber)){
                tileType+=Tile.DARK_INDICATOR;
            }

            if (isRosette){
                tiles.add(new Rosette(tileNumber, tileType));
            }
            else{
                tiles.add(new Tile(tileNumber, tileType));
            }

        }


    }


    /**
     * Returns the sequence of {@code Tile} objects in player's path as described by {@code layout}
     * @param playerColour
     * @return
     */
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
