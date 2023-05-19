package strategy_game_dp.producible.unit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import strategy_game_dp.producible.Producible;
import strategy_game_dp.producible.Tool;
import strategy_game_dp.producible.unit.modifier.UnitModifier;
import strategy_game_dp.world.Cell;
import strategy_game_dp.world.Inventory;
import strategy_game_dp.world.ResourceType;
import strategy_game_dp.world.WorldMap;

public class Unit extends Producible {

  private final int speed;
  private final int hunger;
  private final List<UnitModifier> modifiers;
  private int x;
  private int y;
  private int xp = 0;
  private boolean canMine;
  private Tool tool;

  public Unit(
      @NotNull Map<ResourceType, Integer> cost,
      final int x,
      final int y,
      final @NotNull List<UnitModifier> modifiers) {
    super(cost);
    this.x = x;
    this.y = y;
    this.speed = 1;
    this.hunger = 1;
    this.canMine = true;
    this.modifiers = modifiers;
    for (final UnitModifier modifier : this.modifiers) {
      modifier.setUnit(this);
    }
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

  public int getEfficiency() {
    AtomicInteger efficiency = new AtomicInteger(1);
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

  public void setXp(final int xp) {
    this.xp = xp;
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

  public List<UnitModifier> getActiveModifiers() {
    return modifiers.stream().filter(UnitModifier::isActive).collect(Collectors.toList());
  }

  public void mine() {
    if (canMine()) {
      Cell cell = WorldMap.getInstance().getCell(getX(), getY());
      ResourceType resourceType = cell.getType();
      Inventory.getInstance().add(resourceType, cell.mine(getEfficiency()));
    }
  }

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

  public void eat() {
    setCanMine(Inventory.getInstance().get(ResourceType.FOOD) > getHunger());
    if (canMine()) Inventory.getInstance().remove(ResourceType.FOOD, getHunger());
  }
}
