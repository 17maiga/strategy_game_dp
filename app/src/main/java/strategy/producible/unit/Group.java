package strategy.producible.unit;

import java.util.*;
import strategy.producible.Tool;
import strategy.world.WorldMap;

public class Group extends Unit {

  private List<Unit> units;

  public Group(int x, int y) {
    super(x, y, new ArrayList<>());
  }

  public void addUnit(Unit unit) {
    if (unit instanceof Group) units.addAll(((Group) unit).getUnits());
    else units.add(unit);
  }

  public void removeUnit(Unit unit) {
    units.remove(unit);
  }

  public List<Unit> getUnits() {
    return units;
  }

  public void setUnits(List<Unit> units) {
    this.units = units;
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
  public int getEfficiency() {
    return units.stream()
        .filter(Unit::canMine)
        .filter(
            unit ->
                unit.getTool().canMine(WorldMap.getInstance().getCell(getX(), getY()).getType()))
        .mapToInt(unit -> unit.getTool().getEfficiency())
        .max()
        .orElse(0);
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
                u.getTool().getTargets().size() == tool.getTargets().size()
                    && new HashSet<>(u.getTool().getTargets()).containsAll(tool.getTargets()))
        .min(Comparator.comparingInt(Unit::getEfficiency))
        .ifPresent(unit -> unit.setTool(tool));
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
  public boolean mine() {
    return units.stream().allMatch(Unit::mine);
  }

  @Override
  public void eat() {
    units.forEach(Unit::eat);
  }
}
