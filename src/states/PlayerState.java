package states;

public class PlayerState {


    public final Integer playerColour;

    public PlayerState(Integer playerColour){

        this.playerColour = playerColour;
    }


    public PlayerState copyState() {
        return new PlayerState(playerColour);
    }
}
