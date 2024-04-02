package player;

import ai.agent.Agent;
import board.Tile;
import game.UrGame;

import java.util.List;

public class PlayerRemote extends PlayerAI{
    public PlayerRemote(int colour, List<Tile> playerPath, UrGame game) {
        super(colour, Agent.Agents.REMOTE, null, playerPath, game );
    }

}
