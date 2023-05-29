package strategy.producible.unit.modifier;

import strategy.producible.unit.Unit;

/**
 * <b>Class representing a unit modifier.</b>
 *
 * <p>Unit modifiers are used to apply multipliers to unit stats. They can modify the unit's speed,
 * efficiency, and hunger. On unit creation, a list of possible modifiers is provided. The unit will
 * then call the {@link #update()} method each turn, which will determine whether the modifier
 * should be applied or not. When getting a unit's stats, the unit will call the appropriate methods
 * from each active modifier to get the final result.
 */
public abstract class UnitModifier {
  /** <b>The unit this modifier is attached to.</b> */
  protected Unit unit = null;

  /** <b>Whether this modifier is active or not.</b> */
  protected boolean active = false;

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(final Unit unit) {
    this.unit = unit;
  }

  public boolean isActive() {
    return active;
  }

  /**
   * <b>Whether this modifier should be applied as a multiplier for the efficiency stat.</b>
   *
   * <p>Modifiers can either be applied as multipliers or as adders. This method allows the unit to
   * know whether a given modifier will multiply the efficiency stat or add to it (modifiers that
   * multiply the efficiency stat are applied first, then modifiers that add to it).
   */
  public boolean isEfficiencyMultiplier() {
    return false;
  }

  /**
   * <b>Update the efficiency stat.</b>
   *
   * <p>This method is called when the unit needs to know the final efficiency stat. It simply
   * returns the value passed in after applying the modifier.
   *
   * @param efficiency The efficiency stat to modify.
   * @return The modified efficiency stat.
   */
  public int updateEfficiency(final int efficiency) {
    return efficiency;
  }

  /** <b>Whether this modifier should be applied as a multiplier for the speed stat.</b> */
  public boolean isSpeedMultiplier() {
    return false;
  }

  /**
   * <b>Update the speed stat.</b>
   *
   * <p>This method is called when the unit needs to know the final speed stat. It simply returns
   * the value passed in after applying the modifier.
   *
   * @param speed The speed stat to modify.
   * @return The modified speed stat.
   */
  public int updateSpeed(final int speed) {
    return speed;
  }

  /** <b>Whether this modifier should be applied as a multiplier for the hunger stat.</b> */
  public boolean isHungerMultiplier() {
    return false;
  }

  /**
   * <b>Update the hunger stat.</b>
   *
   * <p>This method is called when the unit needs to know the final hunger stat. It simply returns
   * the value passed in after applying the modifier.
   *
   * @param hunger The hunger stat to modify.
   * @return The modified hunger stat.
   */
  public int updateHunger(final int hunger) {
    return hunger;
  }

  /**
   * <b>Toggle the unit modifier's status if need be.</b>
   *
   * <p>This function is called each turn, after every other action the unit has taken. It is used
   * to determine whether the modifier should be active or not. For example, the {@link
   * strategy.producible.unit.modifier.ExpertModifier} will only be active if the unit has enough XP
   * and is able to mine.
   */
  public void update() {}

  public abstract String indicator();
}
