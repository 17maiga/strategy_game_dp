package strategy_game_dp.world;

public class Cell {
    public int x;
    public int y;
    public int resourceCount;
    public ResourceType type;

    public Cell(int x, int y, int resourceCount, ResourceType type) {
        this.x = x;
        this.y = y;
        this.resourceCount = resourceCount;
        this.type = type;
    }
}
