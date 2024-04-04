package board;

import exceptions.IllegalMoveException;
import player.Piece;
import player.Player;
import states.BoardState;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
                new int[]{8, 3}, //dims
                new int[]{1, 3, 11, 19, 21}, //rosette tiles
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
     * Map of {@code Player} paths. Key is value of {@link Player#getPlayerColour() Player.colour}
     * Populated lazily through {@link #getPlayerPath(int) getPlayerPath}
     */
    Map<Integer, List<Tile>> playerPaths;

    /**
     * Board constructor. Calls {@code generateTiles} to create board tiles
     */
    public Board(){
        this.playerPaths= new HashMap<>();
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
                tiles.add(new Rosette(this, tileNumber, tileType));
            }
            else{
                tiles.add(new Tile(this, tileNumber, tileType));
            }

        }else{ //Add non-walkable tiles to set if they are PreStartTile or PostEndTile
            if (tileNumber == layout.lightPath()[0]){
                tiles.add(new PreStartTile(this, tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.darkPath()[0]) {
                tiles.add(new PreStartTile(this, tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.lightPath()[layout.lightPath().length-1]) {
                tiles.add(new PostEndTile(this, tileNumber, Tile.LIGHT_INDICATOR));
            } else if (tileNumber == layout.darkPath()[layout.darkPath().length-1]) {
                tiles.add(new PostEndTile(this, tileNumber, Tile.DARK_INDICATOR));
            }
        }


    }

    /**
     * Returns the sequence of {@code Tile} objects in player's path as described by {@code layout}
     * Adds path to {@code playerPaths} if not already present
     * @param playerColour Colour of player. One of {@link Player#LIGHT_PLAYER}={@link Tile#LIGHT_INDICATOR} or {@link Player#DARK_PLAYER}={@link Tile#DARK_INDICATOR}
     * @return List of {@code Tile} objects in player's path
     */
    public List<Tile> getPlayerPath(int playerColour) {
        playerPaths.putIfAbsent(playerColour, findPlayerPath(playerColour));
        return playerPaths.get(playerColour);
    }

    /**
     * Determines the sequence of {@code Tile} objects in player's path as described by {@code layout}
     * @param playerColour Colour of player. One of {@link Player#LIGHT_PLAYER}={@link Tile#LIGHT_INDICATOR} or {@link Player#DARK_PLAYER}={@link Tile#DARK_INDICATOR}
     * @return List of {@code Tile} objects in player's path
     */
    public List<Tile> findPlayerPath(int playerColour) {
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

    /**
     * Moves all pieces back to their {@code Player} owner's {@code PreStartTile}
     * @param pieces Pieces to move to {@code PreStartTile}
     * @throws IllegalMoveException If {@code Piece} cannot be mved back to {@code PreStartTile}
     */
    public void sendPiecesToPreBoard(Set<Piece> pieces) throws IllegalMoveException {
        Map<Player, List<Piece>> piecesByPlayer = pieces.stream().collect(groupingBy(p -> p.getPlayer()));
        for (Map.Entry<Player, List<Piece>> playerPieces : piecesByPlayer.entrySet()) {
            Tile preBoard = getPlayerPath(playerPieces.getKey().getPlayerColour()).get(0);
            for (Piece piece : playerPieces.getValue()) {
                piece.move(preBoard);
            }
        }
    }

    /**
     * Gets first tile in path ( {@code PreStartTile} ) and last tile in path ({@code PostEndTile}) for each path on board
     * @return Collection of first and last tiles in paths
     */
    public List<Tile> getPrePostBoardTiles() {
        List<Tile> prePostTiles = new ArrayList<>();
        for (List<Tile> path : playerPaths.values()) {
            prePostTiles.add(path.get(0));
            prePostTiles.add(path.get(path.size()-1));
        }
        return prePostTiles;
    }

    public BoardState bundle() {
        BoardState currentState = new BoardState();
        currentState.setTileStates(tiles.stream().map(t->t.bundle()).collect(Collectors.toList()));
        currentState.convertAndSetPlayerPathsToState(this.playerPaths);
        return currentState;
    }

    /**
     * Returns the number of rows in the {@code layout} for this board
     * @return {@link BoardLayout#dimensions()} rows value
     */
    public int getRows() {
        return layout.dimensions()[0];
    }

    /**
     * Returns the number of columns in the {@code layout} for this board
     * @return {@link BoardLayout#dimensions()} columns value
     */
    public int getColumns() {
        return layout.dimensions()[1];
    }

    public Tile getTileFromNumber(int tileNumber) {
        return tiles.stream().filter(t->t.getTileNum()==tileNumber).findFirst().orElse(null);
    }
}
