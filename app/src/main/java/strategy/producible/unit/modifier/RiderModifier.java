package strategy.producible.unit.modifier;

public class RiderModifier extends UnitModifier {
  @Override
  public boolean isSpeedMultiplier() {
    return true;
  }

  @Override
  public int updateSpeed(final int speed) {
    return 2 * speed;
  }

  @Override
  public int updateHunger(final int hunger) {
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
