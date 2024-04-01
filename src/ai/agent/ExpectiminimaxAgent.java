package ai.agent;

import ai.metric.Metric;
import board.Tile;
import game.UrGame;
import player.Player;
import player.PlayerAI;
import states.GameState;
import states.TileState;

import java.util.*;
import java.util.stream.IntStream;

public class ExpectiminimaxAgent extends Agent {

    /**
     * Depth of tree to explore by EASY difficulty agent
     */
    private static final int EASY_DEPTH = 2;
    /**
     * Depth of tree to explore by HARD difficulty agent
     */
    private static final int HARD_DEPTH =4;

    /**
     * Factory method for new {@code ExpectminimaxAgent} a specified difficulty in {@code level}
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game   Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     * @param metric Metric to be used to evaluate game states
     * @param level Diffuculty level of opponent to be converted to depth of search (game tree)
     * @return new instance of {@code ExpectiminimaxAgent}
     */
    public static Agent createAgent(PlayerAI player, UrGame game, Metric metric, Level level) {
        switch (level){
            case EASY -> {
                return new ExpectiminimaxAgent(player, game, metric, EASY_DEPTH);
            }case HARD -> {
                return new ExpectiminimaxAgent(player, game, metric, HARD_DEPTH);
            }
        }
        return null;
    }

    /**
     * Difficulty level of agent. Corresponds to depth of game tree used in expectiminimax algorithm.
     */
    public enum Level{
        EASY, HARD
    }

    /**
     * Depth of game tree used by expectiminimax
     */
    private final int DEPTH;
    /**
     * Colour of this ai player
     */
    private final int playerColour;

    /**
     * Probability roll value at index
     */
    private final double[] rollProbabilities;

    /**
     * Metric to be used to evaluate game state
     */
    private final Metric metric;

    /**
     * Metric scores with 'good' state for light player being larger value and 'good' state for dark player being smaller value
     * algorithm implemented with goal of maximising state for the current player.
     * Therefore, if {@code playerColour} is {@link Player#DARK_PLAYER}, must apply multiplier -1 to scores from {@code metric} such that maximising the score by the algorithm will find best state for dark player not light player
     * If {@code playerColour} is {@link Player#LIGHT_PLAYER}, multiplier is 1
     */
    private final int METRIC_MULTIPLIER;

    /**
     * Constructor for new {@code ExpectiminimaxAgent}
     *
     * @param player Reference to {@code Player} instance for whom to serve as an ai agent
     * @param game   Reference to current instance of {@code UrGame} to provide access to information that may be required by {@code Agent} in choosing next move
     * @param metric Metric to be used to evaluate game states
     * @param depth Depth of search (game tree)
     */
    public ExpectiminimaxAgent(PlayerAI player, UrGame game, Metric metric, int depth) {
        super(player, game);
        this.DEPTH = depth;
        this.playerColour = player.getPlayerColour();
        rollProbabilities = game.getRollProbabilities();
        this.metric = metric;
        this.METRIC_MULTIPLIER = playerColour == Player.LIGHT_PLAYER? 1: -1;
    }


    /**
     * Determines and returns end {@code Tile} for next move via expectiminimax algorithm for given value of {@code roll}
     * @param roll Value of roll for turn
     * @return {@code Tile} instance a {@code Piece} is to be moved to in current turn
     */
    @Override
    public Tile determineNextMove(int roll) {
        if (roll ==0){
            return null;
        }
        GameState currentGameState = game.bundle();
        List<TileState[]> possibleMoves = currentGameState.getMovesForPlayerForState(roll);
        if (possibleMoves.isEmpty()){
            return null;
        }

        Map<TileState[], Double> moveWithWeight = new HashMap<>();
        for (TileState[] possibleMove : possibleMoves) {
            GameState stateForMove = currentGameState.copyState();
            stateForMove.evolve(possibleMove);
            moveWithWeight.put(possibleMove, expectiminimax(stateForMove, DEPTH));
        }
        //select maximum move from children
        TileState[] bestMove = moveWithWeight.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).orElse(null).getKey();
        return game.getTileFromNumber(bestMove[1].getTileNum());

    }


    /**
     * Executes expectiminimax recursively, each no-base case recursion includes chance node and min/max nodes
     * @param current Current state of game - just as the last player has moved
     * @param depth Depth of tree reached (0 being deeper than 1 deeper than 2 etc.) Depth of 0 is terminal, base case depth
     * @return If base case: evaluation of {@code current} for agent's {@code metric} with {@code METRIC_MULTIPLIER} applied
     *         If backtracking through tree: The value of chance node which is weighted average of the roll weights.
     *                                       The roll weights are the max/min value of the base case's game states resulting from further recursion of current state for each of the valid moves for a particular roll value
     */
   private double expectiminimax(GameState current, int depth){
        //begins with state being just after last player has moved
        if (depth == 0){
            return METRIC_MULTIPLIER*metric.scoreForState(current); //how good is this state
        } else{

            current.incrementActivePlayer(); //evaluating for next turn
            double[] weightByRoll = new double[rollProbabilities.length];
            for (int i = 0; i < rollProbabilities.length; i++) { //function starts at chance node, examines each roll possibility
                List<TileState[]> possibleMovesForRoll = current.getMovesForPlayerForState(i); //gets possible moves to transition from current game state to next state given a particular value for roll
                if (possibleMovesForRoll.isEmpty()){ //if there are no possible moves for the value of roll - we effectively have rolled 0
                    weightByRoll[i] = weightByRoll[0]; //should be safe as always have potential moves when roll 0
                }
                else {
                    List<Double> weightsForMovesForRoll = new ArrayList<>();
                    for (TileState[] possibleMoveForRoll : possibleMovesForRoll) { //examine each of these possible new states that result from potential move - pick the 'best' as the value for this roll
                        GameState stateForMove = current.copyState();
                        stateForMove.evolve(possibleMoveForRoll);
                        weightsForMovesForRoll.add(expectiminimax(stateForMove, depth - 1));
                    }
                    boolean maximise = current.getActivePlayer() == this.playerColour;
                    double rollWeight = maximise ? weightsForMovesForRoll.stream().max(Double::compare).get() : weightsForMovesForRoll.stream().min(Double::compare).get();
                    weightByRoll[i]=rollWeight;
                }
            }
            return IntStream.range(0, weightByRoll.length).mapToDouble(rollValue -> weightByRoll[rollValue]*rollProbabilities[rollValue]).sum();
        }

   }

}
