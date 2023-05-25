package strategy.building;

import java.util.ArrayList;
import java.util.Map;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;

public class UnitBuilding implements IBuilding<Unit> {
  @Override
  public Unit produce() {
    if (Inventory.getInstance().contains(Map.of(ResourceType.GOLD, 10, ResourceType.FOOD, 10))) {
      Inventory.getInstance().remove(Map.of(ResourceType.GOLD, 10, ResourceType.FOOD, 10));
      return new Unit(0, 0, new ArrayList<>());
    } else {
      return null;
    }
  }
}
