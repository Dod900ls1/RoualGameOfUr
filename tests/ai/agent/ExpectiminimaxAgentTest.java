package ai.agent;

import ai.metric.Metric;
import board.Tile;
import controller.GameController;
import controller.MainController;
import game.UrGame;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerAI;
import player.PlayerOptions;
import states.GameState;

public class ExpectiminimaxAgentTest {

    GameState gameState;
    PlayerAI expectiminimaxEasyPlayer;
    ExpectiminimaxAgent expectiminimaxEasyAgent;
    PlayerAI expectiminimaxHardPlayer;
    ExpectiminimaxAgent expectiminimaxHardAgent;

    @Before
    public void setUp() throws Exception {
        PlayerOptions[] playerOptions = new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, false, Agent.Agents.EXPECTIMINIMAX_EASY, Metric.Metrics.MAXIMISE_POSTBOARD),
                new PlayerOptions(Player.DARK_PLAYER, false, Agent.Agents.EXPECTIMINIMAX_HARD, Metric.Metrics.MAXIMISE_POSTBOARD)

        };
        UrGame game = new UrGame(playerOptions);
        expectiminimaxEasyPlayer = (PlayerAI) game.getPlayerByColour(Player.LIGHT_PLAYER);
        expectiminimaxEasyAgent = (ExpectiminimaxAgent) expectiminimaxEasyPlayer.getAgent();
        expectiminimaxHardPlayer = (PlayerAI) game.getPlayerByColour(Player.DARK_PLAYER);
        expectiminimaxHardAgent = (ExpectiminimaxAgent) expectiminimaxHardPlayer.getAgent();
        game.setActivePlayer(expectiminimaxEasyPlayer);
        gameState = game.bundle();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void determineNextMove() {

        //Tests that pruning and non-pruning agents return the same result
        int roll = 3;
        Tile moveWithoutPruningEasy = expectiminimaxEasyAgent.determineNextMove(roll, false);
        Tile moveWithPruningEasy = expectiminimaxEasyAgent.determineNextMove(roll);
        Assert.assertEquals(moveWithoutPruningEasy, moveWithPruningEasy);

        Tile moveWithoutPruningHard = expectiminimaxHardAgent.determineNextMove(roll, false);
        Tile moveWithPruningHard = expectiminimaxHardAgent.determineNextMove(roll);
        Assert.assertEquals(moveWithoutPruningHard, moveWithPruningHard);
    }

    @Test
    public void countWins(){
        //Testing win frequency compared to random agent for expectiminimax
        GameController gameController = new GameController(new MainController());
        gameController.test = true;
        PlayerOptions[] opts = new PlayerOptions[]{
                new PlayerOptions(Player.LIGHT_PLAYER, false, Agent.Agents.EXPECTIMINIMAX_HARD, Metric.Metrics.MAXIMISE_POSTBOARD),
                new PlayerOptions(Player.DARK_PLAYER, false, Agent.Agents.RANDOM, Metric.Metrics.MAXIMISE_POSTBOARD)};
        int[] overall = null;
        overall = winCountsForThread(gameController, opts);
        for (int i = 0; i < overall.length; i++) {
            System.out.println(i+":"+overall[i]);
        }


    }

    private int[] winCountsForThread(GameController gc, PlayerOptions[] opts){
        int[] threadWinners = new int[2];
        for (int i = 0; i < 20; i++) {
            try {
                gc.createGame(opts);
                int winner = gc.beginGame()-1;
                threadWinners[winner]++;

            }catch(Exception e){

            }
        }
        return threadWinners;
    };
}