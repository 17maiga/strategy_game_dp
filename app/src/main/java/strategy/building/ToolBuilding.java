package strategy.building;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import strategy.Config;
import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class ToolBuilding implements IBuilding {

  private final List<ResourceType> targets;
  private BuildingStatus buildingStatus;
  private int productionTime;

  public ToolBuilding(List<ResourceType> targets) {
    this.targets = targets;
    productionTime = Config.BUILDING_PRODUCTION_TIME;
    buildingStatus = BuildingStatus.IN_PROGRESS;
  }

  @Override
  public void produce(final WorldMap worldMap, final Inventory inventory) {
    if (productionTime > 0) {
      productionTime--;
      buildingStatus = BuildingStatus.IN_PROGRESS;
      return;
    }
    productionTime = Config.BUILDING_PRODUCTION_TIME;
    int efficiency = Config.TOOL_BASE_EFFICIENCY;
    Unit unit =
        worldMap.getUnits().stream()
            .filter(u -> u.getTool() == null)
            .findFirst()
            .orElse(
                worldMap.getUnits().stream()
                    .filter(
                        u ->
                            u.getTool().targets().size() == targets.size()
                                && new HashSet<>(u.getTool().targets()).containsAll(targets))
                    .min(Comparator.comparingInt(u -> u.getTool().efficiency()))
                    .orElse(null));
    if (unit != null) {
      Map<ResourceType, Integer> cost = new java.util.HashMap<>(Config.TOOL_COST);
      if (unit.getTool() != null) {
        cost.keySet().forEach(k -> cost.put(k, cost.get(k) * unit.getTool().efficiency() + 1));
      }
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
    return "Tool factory ("
        + Config.JOBS.entrySet().stream()
            .filter(
                e ->
                    targets.size() == e.getValue().size()
                        && new HashSet<>(targets).containsAll(e.getValue()))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(targets.size() > 0 ? targets.toString() : "Unemployed")
        + "): "
        + switch (buildingStatus) {
          case PRODUCED -> "produced last turn";
          case NOT_ENOUGH_RESOURCES -> "not enough resources";
          case IN_PROGRESS -> "in progress";
        };
  }
}
