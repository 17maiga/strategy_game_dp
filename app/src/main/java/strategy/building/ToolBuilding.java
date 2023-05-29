package strategy.building;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ToolBuilding implements IBuilding {

  private final List<ResourceType> targets;
  private int productionTime;

  @Contract(pure = true)
  public ToolBuilding(List<ResourceType> targets) {
    this.targets = targets;
    this.productionTime = 2;
  }

  @Override
  public void produce(final WorldMap worldMap, final @NotNull Inventory inventory) {
    if (productionTime > 0) {
      productionTime--;
      return;
    }
    productionTime = 2;
    Map<ResourceType, Integer> cost = Map.of(ResourceType.WOOD, 1, ResourceType.ROCK, 1);
    if (inventory.containsResources(cost)) {
      int efficiency;
      Unit unit =
          worldMap.getUnits().stream().filter(u -> u.getTool() == null).findFirst().orElse(null);
      if (unit != null) {
        efficiency = 1;
        unit.setTool(new Tool(efficiency, targets));
        System.out.println(
            "Tool factory for "
                + targets
                + " gave a tool to unemployed unit with efficiency "
                + efficiency
                + ".");
        inventory.removeResources(cost);
        return;
      }
      unit =
          worldMap.getUnits().stream()
              .filter(
                  u ->
                      u.getTool().getTargets().size() == targets.size()
                          && new HashSet<>(u.getTool().getTargets()).containsAll(targets))
              .min(Comparator.comparingInt(u -> u.getTool().getEfficiency()))
              .orElse(null);
      if (unit != null) {
        efficiency = unit.getTool().getEfficiency() + 1;
        unit.setTool(new Tool(efficiency, targets));
        System.out.println(
            "Tool factory for " + targets + " upgraded a unit to " + efficiency + ".");
        inventory.removeResources(cost);
        return;
      }
      System.out.println(
          "Tool factory for "
              + targets
              + " did not find a unit to upgrade. No resources were spent.");
      return;
    }
    System.out.println("Tool factory for " + targets + " did not have enough resources.");
  }
}
