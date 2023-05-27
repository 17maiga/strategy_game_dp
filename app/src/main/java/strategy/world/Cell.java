package strategy.world;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import strategy.producible.unit.Group;
import strategy.producible.unit.Unit;

/**
 * <b>A cell in the world map.</b>
 *
 * <p>Each cell has a {@link ResourceType} and an amount of that resource. It can also contain a
 * {@link Unit}.
 */
public class Cell {
  private final int x;
  private final int y;
  private final ResourceType type;
  private int amount;
  private Unit unit;

  public Cell(final int x, final int y) {
    this.x = x;
    this.y = y;
    type = ResourceType.getRandomType();
    amount =
        Math.random() < type.getSpawnChance()
            ? (int) Math.floor(Math.random() * type.getMaxVeinSize())
            : 0;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public ResourceType getType() {
    return type;
  }

  public int getAmount() {
    return amount;
  }

  public @Nullable Unit getUnit() {
    return unit;
  }

  public void setUnit(final Unit unit) {
    this.unit = unit;
  }

  /**
   * Inserts a unit into this cell.
   *
   * <p>If the cell already contains a unit, it will be grouped with the new unit.
   *
   * @param unit the unit to insert
   */
  public void insertUnit(final Unit unit) {
    if (this.unit == null) {
      this.unit = unit;
    } else {
      if (this.unit instanceof Group) {
        ((Group) this.unit).addUnit(unit);
      } else {
        final Group group = new Group(x, y);
        group.addUnit(this.unit);
        group.addUnit(unit);
        this.unit = group;
      }
    }
  }

  /**
   * Mine resources from this cell.
   *
   * @param amount the amount to mine
   * @return the amount mined
   */
  public int mine(final int amount) {
    if (this.amount > amount) {
      this.amount -= amount;
      return amount;
    } else {
      final int mined = this.amount;
      this.amount = 0;
      return mined;
    }
  }

  /**
   * Finds the closest cell of one of the given types in the world map.
   *
   * @param types the types to find
   * @return the closest cell of one of the given types
   */
  public Cell findClosest(List<ResourceType> types) {
    Queue<Cell> queue = new ArrayDeque<>();
    List<Cell> visited = new ArrayList<>();
    queue.add(this);
    visited.add(this);
    while (!queue.isEmpty()) {
      Cell cell = queue.poll();
      if (types.contains(cell.getType()) && cell.getAmount() > 0) {
        return cell;
      }
      checkNeighbours(queue, visited, cell);
    }
    return null;
  }

  /**
   * Checks the neighbours of the given cell and adds them to the queue if they haven't been visited
   *
   * @param queue the queue to add the neighbours to
   * @param visited the list of visited cells
   * @param cell the cell to check the neighbours of
   */
  private void checkNeighbours(Queue<Cell> queue, List<Cell> visited, @NotNull Cell cell) {
    if (cell.getX() > 0) {
      Cell left = WorldMap.getInstance().getCell(cell.getX() - 1, cell.getY());
      if (!visited.contains(left)) {
        queue.add(left);
        visited.add(left);
      }
    }
    if (cell.getX() < WorldMap.getInstance().width() - 1) {
      Cell right = WorldMap.getInstance().getCell(cell.getX() + 1, cell.getY());
      if (!visited.contains(right)) {
        queue.add(right);
        visited.add(right);
      }
    }
    if (cell.getY() > 0) {
      Cell top = WorldMap.getInstance().getCell(cell.getX(), cell.getY() - 1);
      if (!visited.contains(top)) {
        queue.add(top);
        visited.add(top);
      }
    }
    if (cell.getY() < WorldMap.getInstance().height() - 1) {
      Cell bottom = WorldMap.getInstance().getCell(cell.getX(), cell.getY() + 1);
      if (!visited.contains(bottom)) {
        queue.add(bottom);
        visited.add(bottom);
      }
    }
  }
}
