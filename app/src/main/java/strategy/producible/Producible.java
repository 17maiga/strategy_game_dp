package strategy.producible;

import java.util.Map;
import strategy.world.ResourceType;

public abstract class Producible {
  /**
   * @return the cost of the producible object.
   */
  public abstract Map<ResourceType, Integer> getCost();
}
