package strategy.building;

import java.util.ArrayList;
import java.util.Map;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class UnitBuilding implements IBuilding<Unit> {
  @Override
  public Unit produce(final WorldMap worldMap, final Inventory inventory) {
    if (inventory.containsResources(Map.of(ResourceType.GOLD, 10, ResourceType.FOOD, 10))) {
      inventory.removeResources(Map.of(ResourceType.GOLD, 10, ResourceType.FOOD, 10));
      return new Unit(0, 0, new ArrayList<>());
    } else {
      return null;
    }
  }
}
