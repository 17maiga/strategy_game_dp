package strategy.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import strategy.Game;
import strategy.Utils;
import strategy.producible.unit.Unit;

/**
 * Class representing a world map.
 *
 * <p>The world map is a 2D grid of {@link Cell}s.
 */
public record WorldMap(int width, int height, List<List<Cell>> cells) {

  public WorldMap(final int width, final int height) {
    this(width, height, new ArrayList<>());
    for (int i = 0; i < height; i++) {
      List<Cell> row = new ArrayList<>();
      for (int j = 0; j < width; j++) {
        Cell cell = new Cell(j, i);
        row.add(j, cell);
      }
      cells.add(i, row);
    }
  }

  public Cell getCell(final int x, final int y) {
    int newX = Utils.clamp(x, 0, cells.get(0).size() - 1);
    int newY = Utils.clamp(y, 0, cells.size() - 1);
    return cells.get(newY).get(newX);
  }

  /**
   * Inserts units into the world map.
   *
   * @param units the units to insert
   */
  public void insertUnits(final @NotNull List<Unit> units) {
    units.forEach(unit -> cells.get(unit.getY()).get(unit.getX()).insertUnit(unit));
  }

  public Game.Status turn(Inventory inventory) {
    getUnits().stream().filter(u -> !u.hasPlayed()).forEach(u -> u.turn(this, inventory));
    getUnits().forEach(u -> u.setHasPlayed(false));
    if (cells.stream().allMatch(row -> row.stream().allMatch(cell -> cell.getAmount() == 0))) {
      return Game.Status.WON;
    }
    if (getUnits().stream().noneMatch(Unit::canMine)) {
      return Game.Status.LOST;
    }
    return Game.Status.RUNNING;
  }

  public List<Unit> getUnits() {
    return cells.stream()
        .flatMap(List::stream)
        .map(Cell::getUnit)
        .filter(Objects::nonNull)
        .toList();
  }
}
