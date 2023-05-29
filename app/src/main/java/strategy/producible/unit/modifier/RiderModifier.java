package strategy.producible.unit.modifier;

import strategy.Config;

public class RiderModifier extends UnitModifier {
  @Override
  public boolean isSpeedMultiplier() {
    return true;
  }

  @Override
  public int updateSpeed(final int speed) {
    return speed * Config.RIDER_SPEED_MULTIPLIER;
  }

  @Override
  public int updateHunger(final int hunger) {
    return hunger + Config.RIDER_HUNGER_INCREASE;
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

  @Override
  public String indicator() {
    return "R";
  }
}
