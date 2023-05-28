package strategy.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy.Game;
import strategy.producible.unit.Unit;

/**
 * Singleton class that represents the world map.
 *
 * <p>The world map is a 2D grid of {@link Cell}s.
 */
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
    int newX = Math.min(Math.max(0, x), cells.get(0).size() - 1);
    int newY = Math.min(Math.max(0, y), cells.size() - 1);
    return cells.get(newY).get(newX);
  }

  /**
   * Inserts units into the world map.
   *
   * @param units the units to insert
   */
  public void insertUnits(@NotNull List<Unit> units) {
    units.forEach(unit -> cells.get(unit.getY()).get(unit.getX()).insertUnit(unit));
  }

  public Game.Status turn() {
    getUnits().forEach(Unit::turn);
    if (cells.stream().allMatch(row -> row.stream().allMatch(cell -> cell.getAmount() == 0))) {
      return Game.Status.WON;
    }
    if (getUnits().stream().noneMatch(Unit::canMine)) {
      return Game.Status.LOST;
    }
    return Game.Status.RUNNING;
  }

  public @NotNull List<Unit> getUnits() {
    return cells.stream()
        .flatMap(List::stream)
        .map(Cell::getUnit)
        .filter(Objects::nonNull)
        .toList();
  }
}
