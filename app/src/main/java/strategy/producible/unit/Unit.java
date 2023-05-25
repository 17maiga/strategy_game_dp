package strategy.producible.unit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import strategy.producible.Producible;
import strategy.producible.Tool;
import strategy.producible.unit.modifier.UnitModifier;
import strategy.world.Cell;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

/**
 * Class representing a unit in the game.
 *
 * <p>Units are the main way to interact with the game world. They can move around the map and mine
 * resources.
 *
 * <p>Units can be modified by {@link UnitModifier}s. These modifiers can change the unit's speed,
 * hunger, and mining ability.
 *
 * <p>Units are also equipped with {@link Tool}s. These tools allow the unit to mine resources.
 *
 * <p>Units can be grouped together in {@link Group}s. Groups allow units to move together and mine
 * resources together more efficiently.
 */
public class Unit extends Producible {

  /** How many tiles the unit can move each turn. */
  private final int speed;

  /** How much the unit needs to eat each turn. */
  private final int hunger;

  /** The modifiers that can affect this unit. */
  private final List<UnitModifier> modifiers;

  private int x;
  private int y;
  private int xp = 0;

  /** Whether the unit can mine resources. */
  private boolean canMine;

  /** The tool the unit is equipped with. */
  private Tool tool;

  public Unit(final int x, final int y, final @NotNull List<UnitModifier> modifiers) {
    this.x = x;
    this.y = y;
    this.speed = 5;
    this.hunger = 1;
    this.canMine = true;
    this.modifiers = modifiers;
    for (final UnitModifier modifier : this.modifiers) modifier.setUnit(this);
  }

  @Override
  public Map<ResourceType, Integer> getCost() {
    return Map.of(ResourceType.FOOD, 10, ResourceType.GOLD, 10);
  }

  public int getX() {
    return x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  /**
   * <b>Get the unit's speed, taking into account all active modifiers.</b>
   *
   * @return the unit's speed
   */
  public int getSpeed() {
    AtomicInteger speed = new AtomicInteger(this.speed);
    getActiveModifiers().stream()
        .filter(UnitModifier::isSpeedMultiplier)
        .forEach(modifier -> speed.set(modifier.updateSpeed(speed.get())));
    getActiveModifiers().stream()
        .filter(modifier -> !modifier.isSpeedMultiplier())
        .forEach(modifier -> speed.set(modifier.updateSpeed(speed.get())));
    return speed.get();
  }

  /**
   * <b>Get the unit's hunger, taking into account all active modifiers.</b>
   *
   * @return the unit's hunger
   */
  public int getHunger() {
    AtomicInteger hunger = new AtomicInteger(this.hunger);
    getActiveModifiers().stream()
        .filter(UnitModifier::isHungerMultiplier)
        .forEach(modifier -> hunger.set(modifier.updateHunger(hunger.get())));
    getActiveModifiers().stream()
        .filter(modifier -> !modifier.isHungerMultiplier())
        .forEach(modifier -> hunger.set(modifier.updateHunger(hunger.get())));
    return hunger.get();
  }

  /**
   * <b>Get the unit's efficiency, taking into account all active modifiers.</b>
   *
   * @return the unit's efficiency
   */
  public int getEfficiency() {
    if (tool == null) return 0;
    AtomicInteger efficiency = new AtomicInteger(tool.getEfficiency());
    getActiveModifiers().stream()
        .filter(UnitModifier::isEfficiencyMultiplier)
        .forEach(modifier -> efficiency.set(modifier.updateEfficiency(efficiency.get())));
    getActiveModifiers().stream()
        .filter(modifier -> !modifier.isEfficiencyMultiplier())
        .forEach(modifier -> efficiency.set(modifier.updateEfficiency(efficiency.get())));
    return efficiency.get();
  }

  public int getXp() {
    return xp;
  }

  public void addXp(final int xp) {
    this.xp += xp;
  }

  public Tool getTool() {
    return tool;
  }

  public void setTool(final Tool tool) {
    this.tool = tool;
  }

  public boolean canMine() {
    return canMine;
  }

  public void setCanMine(final boolean canMine) {
    this.canMine = canMine;
  }

  /**
   * <b>Get the unit's currently active modifiers.</b>
   *
   * @return the unit's active modifiers
   */
  public List<UnitModifier> getActiveModifiers() {
    return modifiers.stream().filter(UnitModifier::isActive).collect(Collectors.toList());
  }

  /**
   * <b>Mine resources from the cell the unit is on.</b>
   *
   * <p>Units can only mine resources if they are equipped with a tool that can mine the resource.
   *
   * <p>Resources are added to the global {@link Inventory}.
   *
   * @return whether the unit successfully mined
   */
  public boolean mine() {
    if (canMine()) {
      Cell cell = WorldMap.getInstance().getCell(getX(), getY());
      ResourceType resourceType = cell.getType();
      if (tool.getTargets().contains(resourceType) && cell.getAmount() > 0) {
        Inventory.getInstance().add(resourceType, cell.mine(getEfficiency()));
        return true;
      }
    }
    return false;
  }

  /**
   * <b>Move the unit to a new location.</b>
   *
   * <p>If the unit cannot move to the new location, it will move as close as possible.
   *
   * @param x the x coordinate of the new location
   * @param y the y coordinate of the new location
   */
  public void move(final int x, final int y) {
    WorldMap.getInstance().getCell(getX(), getY()).setUnit(null);
    int distance = Math.abs(x - getX()) + Math.abs(y - getY());
    int speed = getSpeed();
    if (distance <= speed) {
      setX(x);
      setY(y);
    } else {
      while (speed > 0) {
        if (Math.abs(x - getX()) > Math.abs(y - getY())) {
          if (x > getX()) setX(getX() + 1);
          else setX(getX() - 1);
        } else {
          if (y > getY()) setY(getY() + 1);
          else setY(getY() - 1);
        }
        speed--;
      }
    }
    WorldMap.getInstance().getCell(getX(), getY()).insertUnit(this);
  }

  /**
   * <b>Feed the unit.</b>
   *
   * <p>Units can only eat if there is at least {@link #getHunger()} food in the global {@link
   * Inventory}. If not, the unit will not be able to mine resources.
   */
  public void eat() {
    setCanMine(Inventory.getInstance().get(ResourceType.FOOD) > getHunger());
    if (canMine()) Inventory.getInstance().remove(ResourceType.FOOD, getHunger());
  }

  public void turn() {
    eat();
    boolean hasMined = mine();
    if (!hasMined) {
      System.out.printf("Unit at %d, %d could not mine!%n", getX(), getY());
      Cell closest =
          WorldMap.getInstance().getCell(getX(), getY()).findClosest(getTool().getTargets().get(0));
      if (closest != null) {
        System.out.printf(
            "Closest %s is at %d, %d%n",
            getTool().getTargets().get(0), closest.getX(), closest.getY());
        System.out.printf(
            "Unit at %d, %d moving to %d, %d%n", getX(), getY(), closest.getX(), closest.getY());
        move(closest.getX(), closest.getY());
        System.out.printf(
            "Unit at %d, %d moved to %d, %d%n", getX(), getY(), closest.getX(), closest.getY());
      }
    } else {
      System.out.printf("Unit at %d, %d mined!%n", getX(), getY());
    }
    for (final UnitModifier modifier : modifiers) modifier.update();
  }
}