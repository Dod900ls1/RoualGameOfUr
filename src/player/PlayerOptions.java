package player;

/**
 * Record for player setup options, e.g. colour, human
 * Data retrieved from UI - sent to game in record
 */
public record PlayerOptions(int playerColour, boolean isHuman) {
}
