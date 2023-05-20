package strategy.world;

import java.util.ArrayDeque;
import java.util.Queue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy.producible.unit.Group;
import strategy.producible.unit.Unit;

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

  public void insertUnit(final Unit unit) {
    if (this.unit == null) {
      this.unit = unit;
    } else {
      if (this.unit instanceof Group) {
        ((Group) this.unit).addUnit(unit);
      } else {
        final Group group = new Group();
        group.addUnit(this.unit);
        group.addUnit(unit);
        this.unit = group;
      }
    }
  }

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

  public Cell findClosest(ResourceType type) {
    Queue<Cell> queue = new ArrayDeque<>();
    queue.add(this);
    while (!queue.isEmpty()) {
      Cell cell = queue.poll();
      if (cell.getType() == type) {
        return cell;
      }
      if (cell.getX() > 0) {
        queue.add(WorldMap.getInstance().getCell(cell.getX() - 1, cell.getY()));
      }
      if (cell.getX() < WorldMap.getInstance().width() - 1) {
        queue.add(WorldMap.getInstance().getCell(cell.getX() + 1, cell.getY()));
      }
      if (cell.getY() > 0) {
        queue.add(WorldMap.getInstance().getCell(cell.getX(), cell.getY() - 1));
      }
      if (cell.getY() < WorldMap.getInstance().height() - 1) {
        queue.add(WorldMap.getInstance().getCell(cell.getX(), cell.getY() + 1));
      }
    }
    return null;
  }
}
