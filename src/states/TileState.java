package states;

import player.Player;

import java.util.Map;

public class TileState{
    private final int tileNum;
    private Map<Player, Integer> piecesByPlayer;

    public TileState(int tileNum, Map<Player, Integer> piecesByPlayer) {
        this.tileNum = tileNum;
        this.piecesByPlayer = piecesByPlayer;
    }

    public boolean hasPieceForPlayer(Player player){
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

    public boolean canMoveTo(Player player) {
        boolean onPath = player.getPlayerPath().stream().anyMatch(t -> t.getTileNum() == tileNum);
        return true;
        //TODO
    }
}
