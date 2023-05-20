package strategy_game_dp.producible;

import java.util.List;
import java.util.Map;
import strategy_game_dp.world.ResourceType;

public class Tool extends Producible {

  private final int efficiency;
  private final List<ResourceType> targets;

  public Tool(Map<ResourceType, Integer> cost, int efficiency, List<ResourceType> targets) {
    super(cost);
    this.efficiency = efficiency;
    this.targets = targets;
  }

  public int getEfficiency() {
    return efficiency;
  }

  public List<ResourceType> getTargets() {
    return targets;
  }

  public boolean canMine(final ResourceType type) {
    return this.targets.contains(type);
  }
}
