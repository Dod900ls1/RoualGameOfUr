package game;


public class UrGame {

    public static void main(String[] args) {

        Tile[] gameGrid = setupGrid();
        
    }


    public static Tile[] setupGrid() {
        Tile[] grid = new Tile[20];

        for (int i = 0; i < 20; i++) {
            grid[i] = new Tile(i + 1);
        }

        return grid;
    }

    public static void setupPlayer() {
        
    }

}