package strategy.producible;

import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import strategy.world.ResourceType;

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
