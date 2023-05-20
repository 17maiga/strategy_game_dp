package strategy.producible.unit.modifier;

import org.jetbrains.annotations.Nullable;
import strategy.producible.unit.Unit;

/**
 * <b>Class for unit modifiers</b>
 *
 * <p>Unit modifiers are used to apply temporary multipliers to units. They are used to implement
 * modifiers such as the ExpertModifier and the RiderModifier.
 *
 * <p>Unit modifiers are applied to units by calling the unit's addModifier() method. The unit will
 * then call the modifier's update methods each turn, and remove the modifier when it is no longer
 * active.
 */
public class UnitModifier {
  protected @Nullable Unit unit = null;
  protected boolean active = false;

  public @Nullable Unit getUnit() {
    return unit;
  }

  public void setUnit(@Nullable Unit unit) {
    this.unit = unit;
  }

  public boolean isActive() {
    return active;
  }

  public boolean isEfficiencyMultiplier() {
    return false;
  }

  public int updateEfficiency(int efficiency) {
    return efficiency;
  }

  public boolean isSpeedMultiplier() {
    return false;
  }

  public int updateSpeed(int speed) {
    return speed;
  }

  public boolean isHungerMultiplier() {
    return false;
  }

  public int updateHunger(int hunger) {
    return hunger;
  }

  /**
   * <b>Toggle the unit modifier's status if need be.</b>
   *
   * <p>This function is called each turn, after every other action the unit has taken. It is used
   * to update the modifier's internal state, such as the number of turns left.
   */
  public void update() {}
}
