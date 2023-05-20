package strategy.producible.unit.modifier;

import org.jetbrains.annotations.Contract;

public class ExpertModifier extends UnitModifier {

  private int deactivateCountdown;

  @Contract(pure = true)
  public ExpertModifier() {
    deactivateCountdown = 5;
  }

  @Override
  public boolean isEfficiencyMultiplier() {
    return true;
  }

  @Override
  @Contract(pure = true)
  public int updateEfficiency(int efficiency) {
    return efficiency * 2;
  }

  @Override
  public boolean isSpeedMultiplier() {
    return true;
  }

  @Override
  @Contract(pure = true)
  public int updateSpeed(int speed) {
    return (int) Math.floor(speed * 1.5);
  }

  @Override
  public boolean isHungerMultiplier() {
    return true;
  }

  @Override
  @Contract(pure = true)
  public int updateHunger(int hunger) {
    return 2 * hunger;
  }

  @Override
  public void update() {
    if (unit == null) {
      active = false;
      return;
    }
    if (active) {
      if (unit.canMine()) {
        deactivateCountdown = 5;
      } else {
        deactivateCountdown--;
        if (deactivateCountdown == 0) {
          active = false;
          deactivateCountdown = 5;
        }
      }
    } else if (unit.getXp() > 100 && unit.canMine()) {
      active = true;
    }
  }
}
