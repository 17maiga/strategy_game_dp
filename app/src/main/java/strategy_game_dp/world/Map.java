package strategy_game_dp.world;

import java.util.List;

public class Map {
    public int width;
    public int height;
    public List<Cell> cells;

    public Map(int width, int height, List<Cell> cells) {
        this.width = width;
        this.height = height;
        this.cells = cells;
    }
}
