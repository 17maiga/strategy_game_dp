package strategy.producible.unit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import org.jetbrains.annotations.NotNull;
import strategy.Config;
import strategy.producible.Tool;
import strategy.world.Cell;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Group extends Unit {

  private final List<Unit> units;

  public Group(final int x, final int y) {
    super(x, y, new ArrayList<>());
    units = new ArrayList<>();
  }

  public void addUnit(final Unit unit) {
    if (unit instanceof Group) {
      units.addAll(((Group) unit).getUnits());
    } else {
      units.add(unit);
    }
  }

  public void removeUnit(final Unit unit) {
    units.remove(unit);
  }

  public List<Unit> getUnits() {
    return units;
  }

  @Override
  public void setX(int x) {
    super.setX(x);
    units.forEach(unit -> unit.setX(x));
  }

  @Override
  public void setY(int y) {
    super.setY(y);
    units.forEach(unit -> unit.setY(y));
  }

  @Override
  public int getSpeed() {
    return units.stream().mapToInt(Unit::getSpeed).min().orElse(0);
  }

  @Override
  public int getHunger() {
    return units.stream().mapToInt(Unit::getHunger).sum();
  }

  @Override
  public int getEfficiency(final ResourceType type) {
    List<Unit> units =
        this.units.stream()
            .filter(Unit::canMine)
            .filter(unit -> unit.getTool().canMine(type))
            .toList();
    AtomicReference<Float> base =
        new AtomicReference<>((float) units.stream().mapToInt(u -> u.getEfficiency(type)).sum());
    units.forEach(
        u ->
            base.updateAndGet(v -> (float) (v * Config.GROUP_EFFICIENCY_MULTIPLIER)));
    return (int) Math.floor(base.get());
  }

  @Override
  public String getJob() {
    return "Group";
  }

  @Override
  public int getXp() {
    return units.stream().mapToInt(Unit::getXp).sum();
  }

  @Override
  public void addXp(int xp) {
    units.forEach(unit -> unit.addXp(xp));
  }

  @Override
  public Tool getTool() {
    return null;
  }

  @Override
  public void setTool(final Tool tool) {
    units.stream()
        .filter(
            u ->
                u.getTool().targets().size() == tool.targets().size()
                    && new HashSet<>(u.getTool().targets()).containsAll(tool.targets()))
        .min(Comparator.comparingInt(u -> u.getTool().efficiency()))
        .ifPresent(u -> u.setTool(tool));
  }

  @Override
  public boolean canMine() {
    return units.stream().anyMatch(Unit::canMine);
  }

  @Override
  public void setCanMine(boolean canMine) {
    units.forEach(unit -> unit.setCanMine(canMine));
  }

  @Override
  public boolean mine(final WorldMap worldMap, final Inventory inventory) {
    return units.stream().allMatch(u -> u.mine(worldMap, inventory));
  }

  @Override
  public void eat(@NotNull Inventory inventory) {
    units.forEach(u -> u.eat(inventory));
  }

  @Override
  public void turn(final WorldMap worldMap, final Inventory inventory) {
    units.forEach(u -> u.turn(worldMap, inventory));
    List<Unit> toRemove =
        units.stream()
            .filter(u -> !u.canMine())
            .peek(
                u -> {
                  Cell cell = worldMap.getCell(u.getX(), u.getY()).findClosestEmpty(worldMap);
                  if (cell != null) {
                    u.setX(cell.getX());
                    u.setY(cell.getY());
                    cell.insertUnit(u);
                  }
                })
            .toList();
    toRemove.forEach(this::removeUnit);
    units.stream()
        .filter(unit -> unit.getX() != getX() || unit.getY() != getY())
        .toList()
        .forEach(this::removeUnit);
    if (units.size() == 1) {
      Unit unit = units.remove(0);
      worldMap.getCell(getX(), getY()).setUnit(unit);
    } else if (units.isEmpty()) {
      worldMap.getCell(getX(), getY()).setUnit(null);
    }
  }
}
