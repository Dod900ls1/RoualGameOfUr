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
                    "111"+
                    "111",
                    2), //navigable tiles
                new int[]{1, 3, 11, 15, 17}, //rosette tiles
                new int[]{13, 10, 7, 4, 1, 2, 5, 8, 11, 14, 17, 20, 23, 22, 19, 16}, //light path
                new int[]{15, 12, 9, 6, 3, 2, 5, 8, 11, 14, 17, 20, 23, 24, 21, 18} //dark path
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
        int shift = Long.numberOfLeadingZeros(layout.tiles());
        Long tilePositions = Long.divideUnsigned(Long.reverse(layout.tiles()), 1L<<(shift));
        while (tilePositions>0){
            boolean nextTileWalkable = (tilePositions & 1) == 1;
            addNextTile(layout, nextTileWalkable, tileCount);
            tilePositions = Long.divideUnsigned(tilePositions, 2L);
            tileCount++; //increment count even if tile not added
        }
    }


    /**
     * Creates next tile in board as described by {@code layout} and adds to {@code tiles} collection
     * @param layout Layout used to create board
     * @param isWalkable Indicator if next tile will be walkable (i.e. instance of {@code Tile} or {@code Rosette}) or not (i.e. possible an instance of {@code PreStartTile} or {@code PostEndTile})
     * @param tileNumber Number of tile to create -- this may not be the number of last tile created as non-walkable bord spaces are still given numbers and may occur as {@code PreStartTile} or {@code PostEndTile} in {@code tiles}
     */
    private void addNextTile(BoardLayout layout, boolean isWalkable, int tileNumber) {

        if (isWalkable){
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

        }else{ //Add non-walkable tiles to set if they are PreStartTile or PostEndTile
            if (tileNumber == layout.lightPath()[0]){
                tiles.add(new PreStartTile(tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.darkPath()[0]) {
                tiles.add(new PreStartTile(tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.lightPath()[layout.lightPath().length-1]) {
                tiles.add(new PostEndTile(tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.darkPath()[layout.darkPath().length-1]) {
                tiles.add(new PostEndTile(tileNumber, Tile.DARK_INDICATOR));
            }
        }


    }


    /**
     * Returns the sequence of {@code Tile} objects in player's path as described by {@code layout}
     * @param playerColour Colour of player. One of {@link Player#LIGHT_PLAYER}={@link Tile#LIGHT_INDICATOR} or {@link Player#DARK_PLAYER}={@link Tile#DARK_INDICATOR}
     * @return List of {@code Tile} objects in player's path
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

    public BoardLayout getLayout() {
        return this.layout;
    }
}
