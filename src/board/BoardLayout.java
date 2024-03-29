package board;

/**
 * Describes a board layout
 * @param tiles Configuraton of tiles - shown as binary string in {@link Board#boardLayouts} which is parsed to {@code Long}
* @param dimensions Dimensions of board. Indexed as [row, column]
 * @param rosettes Numbers of the tiles to contain a rosette
 * @param lightPath Numbers of the tiles on the light player's path. {@code lightPath[0]} is number of {@link PreStartTile} for light player, {@code lightPath[-1]} is number of {@link PostEndTile} for light player
 * @param darkPath Numbers of the tiles on the dark player's path. {@code darkPath[0]} is number of {@link PreStartTile} for dark player, {@code darkPath[-1]} is number of {@link PostEndTile} for dark player
 */
public record BoardLayout(Long tiles, int[] dimensions, int[] rosettes, int[] lightPath, int[] darkPath) {
}
