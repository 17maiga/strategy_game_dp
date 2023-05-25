package strategy.world;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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

  @Contract(pure = true)
  public Cell(final int x, final int y, final ResourceType type, final int amount) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.amount = amount;
  }

  public Cell(final int x, final int y, final @NotNull ResourceType type) {
    this.x = x;
    this.y = y;
    this.type = type;
    amount =
        Math.random() < type.getSpawnChance()
            ? (int) Math.floor(Math.random() * type.getMaxVeinSize())
            : 0;
  }

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

  public void setAmount(final int amount) {
    this.amount = amount;
  }

  public Unit getUnit() {
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
    if (this.unit == null) this.unit = unit;
    else {
      if (this.unit instanceof Group) ((Group) this.unit).addUnit(unit);
      else {
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
   * Finds the closest cell of the given type in the world map.
   *
   * @param type the type to find
   * @return the closest cell of the given type
   */
  public Cell findClosest(ResourceType type) {
    Queue<Cell> queue = new ArrayDeque<>();
    List<Cell> visited = new ArrayList<>();
    queue.add(this);
    visited.add(this);
    while (!queue.isEmpty()) {
      Cell cell = queue.poll();
      if (cell.getType() == type && cell.getAmount() > 0) return cell;
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
    return null;
  }
}
