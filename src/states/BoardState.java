package states;

import player.Player;

import java.util.List;

public class BoardState {

    private List<TileState> tileStates;


    public List<TileState> getTileStates() {
        return tileStates;
    }

    public void setTileStates(List<TileState> tileStates) {
        this.tileStates = tileStates;
    }

    public boolean isMoveValidForState(Player player, TileState from, TileState to){
        if (from.hasPieceForPlayer(player)){
            if (to.canMoveTo(player)){

            }
        }
        return false;
    }




}
