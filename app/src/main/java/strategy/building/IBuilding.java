package strategy.building;

import strategy.world.Inventory;
import strategy.world.WorldMap;

public interface IBuilding<T> {
  /**
   * @return the producible object.
   */
  T produce(final WorldMap worldMap, final Inventory inventory);
}
