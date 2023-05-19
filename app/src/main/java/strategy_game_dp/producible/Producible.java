package strategy_game_dp.producible;

import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy_game_dp.world.ResourceType;

public abstract class Producible {
  private final Map<ResourceType, Integer> cost;

  @Contract(pure = true)
  public Producible(@NotNull final Map<ResourceType, Integer> cost) {
    this.cost = cost;
  }

  public Map<ResourceType, Integer> getCost() {
    return cost;
  }
}
