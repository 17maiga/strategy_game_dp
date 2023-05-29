package strategy.building;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
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
        buildingStatus = BuildingStatus.PRODUCED;
        return;
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
        unit.setTool(new Tool(efficiency, targets));
        System.out.println(
            "Tool factory for " + targets + " upgraded a unit to " + efficiency + ".");
        inventory.removeResources(cost);
        buildingStatus = BuildingStatus.PRODUCED;
        return;
      }
      System.out.println(
          "Tool factory for "
              + targets
              + " did not find a unit to upgrade. No resources were spent.");
      return;
    }
    System.out.println("Tool factory for " + targets + " did not have enough resources.");
    buildingStatus = BuildingStatus.NOT_ENOUGH_RESOURCES;
  }

  @Override
  public String toString() {
    return switch (buildingStatus) {
      case PRODUCED -> "Tool Factory for " + targets + " (produced last turn)";
      case NOT_ENOUGH_RESOURCES -> "Tool Factory for " + targets + " (not enough resources)";
      case IN_PROGRESS -> "Tool Factory for " + targets + " (in progress)";
    };
  }
}
