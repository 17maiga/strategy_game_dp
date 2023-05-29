package strategy.building;

import strategy.world.Inventory;
import strategy.world.WorldMap;

public interface IBuilding {
  /** Produces a resource with the given {@link WorldMap} and {@link Inventory}. */
  void produce(final WorldMap worldMap, final Inventory inventory);
}
