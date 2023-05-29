package strategy.building;

import strategy.Config;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

import java.util.Map;

public interface IBuilding {
  static Map<ResourceType, Integer> getCost() {
    return Config.BUILDING_COST;
  }

  /** Produces a resource with the given {@link WorldMap} and {@link Inventory}. */
  void produce(final WorldMap worldMap, final Inventory inventory);

  enum BuildingStatus {
    PRODUCED,
    NOT_ENOUGH_RESOURCES,
    IN_PROGRESS
  }
}
