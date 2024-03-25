package board;

/**
 * Describes a board layout
 * @param tiles Configuraton of tiles - shown as binary string in {@link Board#boardLayouts} which is parsed to {@code Long}
 * @param rosettes Numbers of the tiles to contain a rosette
 * @param lightPath Numbers of the tiles on the light player's path
 * @param darkPath Numbers of the tiles on the dark player's path
 */
public record BoardLayout(Long tiles, int[] rosettes, int[] lightPath, int[] darkPath) {
}
