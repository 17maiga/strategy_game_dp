package strategy.building;

import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ToolBuilding implements IBuilding<Tool> {
  @Override
  public Tool produce() {
    if (Inventory.getInstance().contains(Map.of(ResourceType.WOOD, 10, ResourceType.STONE, 10))) {
      Inventory.getInstance().remove(Map.of(ResourceType.WOOD, 10, ResourceType.STONE, 10));
      AtomicInteger efficiency = new AtomicInteger();
      AtomicReference<List<ResourceType>> targets = new AtomicReference<>();
      WorldMap.getInstance().getUnits().stream()
          .map(Unit::getTool)
          .min(Comparator.comparingInt(Tool::getEfficiency))
          .ifPresent(
              tool -> {
                efficiency.set(tool.getEfficiency() + 1);
                targets.set(tool.getTargets());
              });
      return new Tool(efficiency.get(), targets.get());
    } else {
      return null;
    }
  }
}
