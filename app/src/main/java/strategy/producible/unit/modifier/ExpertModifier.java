package strategy.producible.unit.modifier;

import strategy.Config;

/**
 * Expert modifier for units.
 *
 * <p>Increases efficiency, speed, and hunger.
 */
public class ExpertModifier extends UnitModifier {

  /** Countdown to deactivate. */
  private int deactivateCountdown;

  public ExpertModifier() {
    deactivateCountdown = Config.EXPERT_DEACTIVATE_COUNTDOWN;
  }

  @Override
  public boolean isEfficiencyMultiplier() {
    return true;
  }

  @Override
  public int updateEfficiency(final int efficiency) {
    return efficiency * Config.EXPERT_EFFICIENCY_MULTIPLIER;
  }

  @Override
  public boolean isSpeedMultiplier() {
    return true;
  }

  @Override
  public int updateSpeed(final int speed) {
    return (int) Math.floor(speed * Config.EXPERT_SPEED_MULTIPLIER);
  }

  @Override
  public boolean isHungerMultiplier() {
    return true;
  }

  @Override
  public int updateHunger(final int hunger) {
    return hunger * Config.EXPERT_HUNGER_MULTIPLIER;
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
      if (unit.canMine()) deactivateCountdown = Config.EXPERT_DEACTIVATE_COUNTDOWN;
      else {
        deactivateCountdown--;
        if (deactivateCountdown == 0) {
          active = false;
          deactivateCountdown = Config.EXPERT_DEACTIVATE_COUNTDOWN;
        }
      }
    } else if (unit.getXp() > Config.EXPERT_XP_THRESHOLD && unit.canMine()) {
      active = true;
    }
  }

  @Override
  public String indicator() {
    return "E";
  }
}
