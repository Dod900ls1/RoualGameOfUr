package ai.metric;

import states.GameState;

public abstract class Metric {


    /**
     * Evaluates the {@code gameState} and gives it a score based on the criteria of this metric.
     * Scores reflect how 'good' game state is for both players.
     * Game states with <b>HIGHER</b> scores are better for <b>LIGHT</b> player
     * Game states with <b>LOWER</b> scores are better for <b>DARK</b> player
     * @param gameState State to score
     * @return Score for {@code gameState} according to this metric's criteria
     */
    public abstract double scoreForState(GameState gameState);
}
