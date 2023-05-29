package strategy.building;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class ToolBuilding implements IBuilding {

  private final List<ResourceType> targets;
  private BuildingStatus buildingStatus;
  private int productionTime;

  @Contract(pure = true)
  public ToolBuilding(List<ResourceType> targets) {
    this.targets = targets;
    this.productionTime = 2;
    buildingStatus = BuildingStatus.IN_PROGRESS;
  }

  @Override
  public void produce(final WorldMap worldMap, final @NotNull Inventory inventory) {
    if (productionTime > 0) {
      productionTime--;
      buildingStatus = BuildingStatus.IN_PROGRESS;
      return;
    }
    productionTime = 2;
    Map<ResourceType, Integer> cost = Map.of(ResourceType.WOOD, 1, ResourceType.ROCK, 1);
    int efficiency = 1;
    Unit unit =
        worldMap.getUnits().stream().filter(u -> u.getTool() == null).findFirst().orElse(null);
    if (unit != null) {
      if (inventory.containsResources(cost)) {
        unit.setTool(new Tool(efficiency, targets));
        inventory.removeResources(cost);
        buildingStatus = BuildingStatus.PRODUCED;
        return;
      } else {
        buildingStatus = BuildingStatus.NOT_ENOUGH_RESOURCES;
        return;
      }
    }
    unit =
        worldMap.getUnits().stream()
            .filter(
                u ->
                    u.getTool().targets().size() == targets.size()
                        && new HashSet<>(u.getTool().targets()).containsAll(targets))
            .min(Comparator.comparingInt(u -> u.getTool().efficiency()))
            .orElse(null);
    if (unit != null) {
      efficiency = unit.getTool().efficiency() + 1;
      cost = Map.of(ResourceType.WOOD, efficiency, ResourceType.ROCK, efficiency);
      if (inventory.containsResources(cost)) {
        unit.setTool(new Tool(efficiency, targets));
        inventory.removeResources(cost);
        buildingStatus = BuildingStatus.PRODUCED;
      } else {
        buildingStatus = BuildingStatus.NOT_ENOUGH_RESOURCES;
      }
    }
  }

  @Override
  public String toString() {
    return "Tool factory (" +
        Tool.jobs.entrySet().stream()
            .filter(
                e ->
                    targets.size() == e.getValue().size()
                        && new HashSet<>(targets).containsAll(e.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(targets.size() > 0 ? targets.toString() : "Unemployed") +
        "): " +
        switch (buildingStatus) {
          case PRODUCED -> "produced last turn";
          case NOT_ENOUGH_RESOURCES -> "not enough resources";
          case IN_PROGRESS -> "in progress";
        };
  }
}
