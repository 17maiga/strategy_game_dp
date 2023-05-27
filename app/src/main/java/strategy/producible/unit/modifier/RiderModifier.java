package strategy.producible.unit.modifier;

public class RiderModifier extends UnitModifier {
  @Override
  public boolean isSpeedMultiplier() {
    return true;
  }

  @Override
  public int updateSpeed(int speed) {
    return 2 * speed;
  }

  @Override
  public int updateHunger(int hunger) {
    return hunger + 3;
  }

  @Override
  public void update() {
    if (unit == null) {
      active = false;
      return;
    }
    if (active && !unit.canMine()) {
      active = false;
    } else if (unit.canMine()) {
      active = true;
    }
  }
}
