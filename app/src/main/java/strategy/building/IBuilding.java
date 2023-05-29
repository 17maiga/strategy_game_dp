package strategy.building;

import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

import java.util.Map;

public interface IBuilding {
  static Map<ResourceType, Integer> getCost() {
    return Map.of(
        ResourceType.WOOD, 0, ResourceType.GOLD, 0, ResourceType.FOOD, 0, ResourceType.ROCK, 0);
  }

  /** Produces a resource with the given {@link WorldMap} and {@link Inventory}. */
  void produce(final WorldMap worldMap, final Inventory inventory);

  @Override
  String toString();

  enum BuildingStatus {
    PRODUCED,
    NOT_ENOUGH_RESOURCES,
    IN_PROGRESS
  }
}
