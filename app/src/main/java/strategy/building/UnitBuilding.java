package strategy.building;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import strategy.Config;
import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class UnitBuilding implements IBuilding {

  private final List<ResourceType> targets;
  private BuildingStatus buildingStatus = BuildingStatus.IN_PROGRESS;
  private int productionTime = Config.BUILDING_PRODUCTION_TIME;

  public UnitBuilding(List<ResourceType> targets) {
    this.targets = targets;
  }

  @Override
  public void produce(final WorldMap worldMap, final Inventory inventory) {
    if (productionTime > 0) {
      productionTime--;
      buildingStatus = BuildingStatus.IN_PROGRESS;
      return;
    }
    productionTime = Config.BUILDING_PRODUCTION_TIME;
    Map<ResourceType, Integer> cost = Config.UNIT_COST;
    if (inventory.containsResources(cost)) {
      inventory.removeResources(cost);
      Unit unit =
          new Unit(
              (int) (Math.random() * worldMap.width()),
              (int) (Math.random() * worldMap.height()),
              new ArrayList<>());
      if (targets.size() > 0) {
        unit.setTool(new Tool(Config.TOOL_BASE_EFFICIENCY, targets));
      }
      worldMap.insertUnits(List.of(unit));
      buildingStatus = BuildingStatus.PRODUCED;
      return;
    }
    buildingStatus = BuildingStatus.NOT_ENOUGH_RESOURCES;
  }

  @Override
  public String toString() {
    return "Unit factory ("
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
