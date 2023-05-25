package strategy.producible;

import java.util.Map;
import strategy.world.ResourceType;

/**
 * Interface for all producible objects.
 *
 * <p>Producible objects are objects that can be produced by buildings.
 */
public abstract class Producible {
  /**
   * @return the cost of the producible object.
   */
  public abstract Map<ResourceType, Integer> getCost();
}
