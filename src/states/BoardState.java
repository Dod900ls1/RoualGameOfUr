package states;

import board.Tile;
import player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardState {
    public Map<Integer, List<TileState>> playerPaths;
    private List<TileState> tileStates;

    public BoardState() {
        playerPaths = new HashMap<>();
    }

    public List<TileState> getTileStates() {
        return tileStates;
    }

    public void setTileStates(List<TileState> tileStates) {
        this.tileStates = tileStates;
    }


    /**
     * Gets all possible moves for {@code player} for this current {@code BoardState}
     * @param player Player to find moves for as Integer (value of {@link Player#getPlayerColour()})
     * @param roll Value of dice roll for this move
     * @return List of valid move options as 2 element {@link TileState} arrays (indexed [startTile, endTile])
     */
    public List<TileState[]> getMovesForPlayerForState(Integer player, int roll){
        List<TileState> tileStatesInPath = playerPaths.get(player);
        return tileStatesInPath.stream()
                .mapToInt(ts -> tileStatesInPath.indexOf(ts))//convert to index in path
                .mapToObj(tsIndex -> new int[]{tsIndex, tsIndex+roll})//pair start and end indexes
                .filter(tsIndexes -> tsIndexes[1]<tileStatesInPath.size())//end tile index outside of path
                .map(tsIndexes -> new TileState[]{tileStatesInPath.get(tsIndexes[0]), tileStatesInPath.get(tsIndexes[1])})
                .filter(ts -> ts[0].hasPieceForPlayer(player)) //start has piece
                .filter(ts -> ts[1].canMoveTo(player)) //end can be moved to
                .collect(Collectors.toList());

    }


    /**
     * Evolves this instance of {@code BoardState} for the provided {@code potentialMove} executed by {@code player}
     * @param potentialMove Move to be simulated in evolved state
     * @param player Player state representation executing the {@code potentialMove} in evolved state
     * @return This {@code BoardState} after execution of {@code potentialMove} by {@code player}
     */
    public void evolve(TileState[] potentialMove, Integer player) {
        TileState from = potentialMove[0];
        TileState to =potentialMove[1];
        from.removePieceForPlayer(player);
        to.addPieceForPlayer(player);
        sendPiecesToPreBoard(to, player);
    }

    /**
     * Sends all pieces on {@code tile} not belonging to {@code playerToKeep} back to preboard tile in their player's path
     * @param tile
     * @param playerToKeep
     */
    private void sendPiecesToPreBoard(TileState tile, Integer playerToKeep) {
        Map<Integer, Integer> piecesByPlayerOnTile = tile.getPiecesByPlayer();
        for (Map.Entry<Integer, Integer> playerPieces : piecesByPlayerOnTile.entrySet()) {
            int player = playerPieces.getKey();
            if (player!=playerToKeep) {
                TileState preboardTile = playerPaths.get(playerPieces.getKey()).get(0);
                tile.removeAllPiecesForPlayer(player);
                preboardTile.addManyPiecesForPlayer(player, playerPieces.getValue());
            }
        }
    }

    /**
     * Performs deep copy of this board state
     * @return Deep copy of this board state
     */
    public BoardState copyState(){
        BoardState copy = new BoardState();
        List<TileState> copyTiles = tileStates.stream().map(TileState::copyState).toList(); //nb: list unmodifiable
        copy.setTileStates(copyTiles);
        copy.setPlayerPaths(copyPlayerPaths(copyTiles));
        return copy;
    }


    /**
     * Receives map of player paths as list of {@code Tile} instances.
     * Converts {@code Tile} objects to the equivalent {@code TileState} object in this {@code BoardState} from {@link #tileStates} using value of {@link Tile#getTileNum()}
     * Sets converted map as {@link #playerPaths}
     * @param playerPathsAsTiles Map of player paths with keys as lists of {@code Tile} instances
     */
    public void convertAndSetPlayerPathsToState(Map<Integer, List<Tile>> playerPathsAsTiles) {
        for (Map.Entry<Integer, List<Tile>> path : playerPathsAsTiles.entrySet()) {
            List<Tile> tiles = path.getValue();
            List<TileState> tilesAsStates = tiles.stream().map( t->tileStates.stream().filter(ts-> ts.getTileNum()==t.getTileNum()).findFirst().orElse(null)).collect(Collectors.toList());
            this.playerPaths.put(path.getKey(), tilesAsStates);
        }
    }

    private void setPlayerPaths(Map<Integer, List<TileState>> playerPaths){
        this.playerPaths = playerPaths;
    }


    /**
     * Deep copy {@link #playerPaths} converting {@link TileState} objects in path from elements in this {@link #tileStates} to equivalent deep-copied elements of {@code copiedTiles} with same tile number
     * @param copiedTiles Deep-copied {@code tileStates} elements from this board to be used to construct map copy
     * @return deep copy of {@code playerPaths}
     */
   private Map<Integer, List<TileState>> copyPlayerPaths(List<TileState> copiedTiles){
        //Integer immutable, List and TileState must be deep copied
        Map<Integer, List<TileState>> copyPaths = new HashMap<>();
        for (Map.Entry<Integer, List<TileState>> path : playerPaths.entrySet()) {
            copyPaths.put(path.getKey(), //Integer immutable so can be used
                    path.getValue().stream() //Must covert tilestate in this boardstate's path to deep copy of tile state in deepcopy of board state
                            .map(tileState -> copiedTiles.stream()
                                    .filter(copiedState -> copiedState.getTileNum()==tileState.getTileNum())
                            .findFirst().orElse(null)).collect(Collectors.toList()));
        }
        return copyPaths;
   }


    public int getPiecesPostBoardForPlayer(int player) {
       List<TileState> path = playerPaths.get(player);
       TileState postBoardTile = path.get(path.size()-1);
       return postBoardTile.getPiecesForPlayer(player);
    }

    public List<TileState> getPathForPlayer(int player) {
       return playerPaths.get(player);
    }

    public TileState getTileStateByTileNumber(int tileNum) {
       return tileStates.stream().filter(tileState -> tileState.getTileNum()==tileNum).findFirst().orElse(null);
    }
}
