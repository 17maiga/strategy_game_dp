package strategy.producible.unit.modifier;

import org.jetbrains.annotations.Contract;

/**
 * Expert modifier for units.
 *
 * <p>Increases efficiency, speed, and hunger.
 */
public class ExpertModifier extends UnitModifier {

  /** Countdown to deactivate. */
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

  /**
   * <b>Toggles the modifier's active state if necessary.</b>
   *
   * <p>Activates if the unit has more than 100 XP and can mine. Deactivates if the unit has not
   * been able to mine for {@link #deactivateCountdown} turns.
   */
  @Override
  public void update() {
    if (unit == null) {
      active = false;
      return;
    }
    if (active) {
      if (unit.canMine()) deactivateCountdown = 5;
      else {
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
