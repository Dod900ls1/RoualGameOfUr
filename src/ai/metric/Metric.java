package ai.metric;

import game.UrGame;
import states.GameState;

/**
 * Abstract class. Implementors will model a metric that assigns value to a {@link GameState} instance based on some parameters
 */
public abstract class Metric {

    public enum Metrics {
        MAXIMISE_ADVANCEMENT,
        MAXIMISE_POSTBOARD;
    }

    public final double MAX_VALUE, MIN_VALUE;

    protected Metric(double maxValue, double minValue) {
        MAX_VALUE = maxValue;
        MIN_VALUE = minValue;
    }

    public static Metric getNewMetric(Metrics metricType, UrGame game) {
        switch (metricType){
            case MAXIMISE_ADVANCEMENT -> {
                return new MaximiseAdvancement(game);
            }
            case MAXIMISE_POSTBOARD -> {
                return new MaximisePostBoard(game);
            }
        }
        return null;
    }

    /**
     * Evaluates the {@code gameState} and gives it a score based on the criteria of this metric.
     * Scores reflect how 'good' game state is for both players.
     * Game states with <b>HIGHER</b> scores are better for <b>LIGHT</b> player
     * Game states with <b>LOWER</b> scores are better for <b>DARK</b> player
     * @param gameState State to score
     * @return Score for {@code gameState} according to this metric's criteria
     */
    public abstract double scoreForState(GameState gameState);


    public double getMaxValue() {
        return MAX_VALUE;
    }

    public double getMinValue() {
        return MIN_VALUE;
    }

}
