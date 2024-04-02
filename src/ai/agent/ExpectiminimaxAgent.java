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

    private static final int DEFAULT_DEPTH = 3;

    private final int DEPTH;
    private final int playerColour;

    private final double[] rollProbabilities;

    private final Metric metric;

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

    public ExpectiminimaxAgent(PlayerAI player, UrGame game, Metric metric) {
        this(player, game,metric, DEFAULT_DEPTH);
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


   private double expectiminimax(GameState current, int depth){
        //begins with state being just after last player has moved
        if (depth == 0){
            return METRIC_MULTIPLIER*metric.scoreForState(current); //how good is this state for active player of current
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
