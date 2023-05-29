package strategy.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class UnitBuilding implements IBuilding {
  @Override
  public void produce(final WorldMap worldMap, final @NotNull Inventory inventory) {
    Map<ResourceType, Integer> cost = Map.of(ResourceType.GOLD, 1, ResourceType.FOOD, 1);
    if (inventory.containsResources(cost)) {
      inventory.removeResources(cost);
      Unit unit =
          new Unit(
              (int) (Math.random() * worldMap.width()),
              (int) (Math.random() * worldMap.height()),
              new ArrayList<>());
      worldMap.insertUnits(List.of(unit));
      System.out.println("Unit factory produced a unit.");
      return;
    }
    System.out.println("Unit factory does not have enough resources to produce a unit.");
  }
}
