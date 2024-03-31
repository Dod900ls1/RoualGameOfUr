package states;

import player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TileState{
    private final int tileNum;
    private final boolean canHaveMultiplePieces;
    /**
     * Map of number of player pieces on this tile. Key is player number (as in {@code Player.colour}, value is number of pieces belonging to player on this tile.
     * NB: easier to use integers to indicate player as {@code Integer} immutable so can more easily deep-copy {@code TileState} object
     */
    private Map<Integer, Integer> piecesByPlayer;

    public TileState(int tileNum, boolean canContainMultiplePieces) {
        this.tileNum = tileNum;
        this.canHaveMultiplePieces = canContainMultiplePieces;
        this.piecesByPlayer = new HashMap<>();
    }

    public boolean hasPieceForPlayer(Integer player){
        if (piecesByPlayer.containsKey(player)){
            if (piecesByPlayer.get(player)>0){
                return true;
            }
        }
        return false;
    }

    public int getTileNum() {
        return tileNum;
    }

    public boolean canMoveTo(Integer player) {
        return canHaveMultiplePieces||(!hasPieceForPlayer(player));
    }

    public void removePieceForPlayer(Integer player) {
        this.piecesByPlayer.put(player, this.piecesByPlayer.get(player)-1);
    }

    public void addPieceForPlayer(Integer player) {
        this.piecesByPlayer.put(player, this.piecesByPlayer.getOrDefault(player, 0)+1);
    }

    public TileState copyState() {
        Map<Integer, Integer> copyPieces = piecesByPlayer.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        TileState copy = new TileState(tileNum, canHaveMultiplePieces);
        copy.setPiecesByPlayer(copyPieces);
        return copy;
    }


    /**
     * Converts map to {@code Player} object keys to {@code Integer} object keys using {@link Player#getPlayerColour()}
     * Stores converted map as {@link #piecesByPlayer}
     */
    public void convertAndSetPiecesByPlayer(Map<Player, Integer> piecesByPlayerAsPlayer){
        piecesByPlayerAsPlayer.forEach((player, pieceCount) -> this.piecesByPlayer.put(player.getPlayerColour(), pieceCount));
    }


    public void setPiecesByPlayer(Map<Integer, Integer> piecesByPlayer) {
        this.piecesByPlayer = piecesByPlayer;
    }

    public int getPiecesForPlayer(int player) {
        return piecesByPlayer.getOrDefault(player, 0);
    }

    public Map<Integer, Integer> getPiecesByPlayer() {
        return piecesByPlayer;
    }

    public void removeAllPiecesForPlayer(Integer player) {
        piecesByPlayer.put(player, 0);
    }

    public void addManyPiecesForPlayer(Integer player, Integer numberOfPiecesToAdd) {
        this.piecesByPlayer.put(player, this.piecesByPlayer.getOrDefault(player, 0)+numberOfPiecesToAdd);
    }
}
