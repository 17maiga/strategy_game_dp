package strategy.world;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy.producible.unit.Unit;

public record WorldMap(int width, int height, List<List<Cell>> cells) {
  private static WorldMap instance = null;

  public static WorldMap getInstance(int width, int height) {
    if (instance == null) {
      List<List<Cell>> cells = new ArrayList<>();
      for (int i = 0; i < height; i++) {
        List<Cell> row = new ArrayList<>();
        for (int j = 0; j < width; j++) {
          Cell cell = new Cell(j, i);
          row.add(j, cell);
        }
        cells.add(i, row);
      }
      instance = new WorldMap(width, height, cells);
    }
    return instance;
  }

  @Contract(pure = true)
  public static WorldMap getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException();
    }
    return instance;
  }

  public Cell getCell(final int x, final int y) {
    return cells.get(y).get(x);
  }

  public void insertUnits(@NotNull List<Unit> units) {
    for (Unit unit : units) {
      cells.get(unit.getY()).get(unit.getX()).insertUnit(unit);
    }
  }
}
