package strategy.building;

import org.jetbrains.annotations.NotNull;
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
  public Tool produce(final WorldMap worldMap, final @NotNull Inventory inventory) {
    if (inventory.containsResources(Map.of(ResourceType.WOOD, 10, ResourceType.ROCK, 10))) {
      inventory.removeResources(Map.of(ResourceType.WOOD, 10, ResourceType.ROCK, 10));
      AtomicInteger efficiency = new AtomicInteger();
      AtomicReference<List<ResourceType>> targets = new AtomicReference<>();
      worldMap.getUnits().stream()
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
