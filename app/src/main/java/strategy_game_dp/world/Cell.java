package strategy_game_dp.world;

public class Cell {
    public int x;
    public int y;
    public int ressourceCount;
    public RessourceType type;

    public Cell(int x, int y, int ressourceCount, RessourceType type) {
        this.x = x;
        this.y = y;
        this.ressourceCount = ressourceCount;
        this.type = type;
    }
}
