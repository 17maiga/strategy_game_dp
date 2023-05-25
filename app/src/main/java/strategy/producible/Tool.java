package strategy.producible;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import strategy.world.ResourceType;

/**
 * <b>Class representing a tool.</b>
 *
 * <p>Tools are used by units to mine resources. They have an efficiency and a list of resources
 * they can mine. They can be produced by buildings.
 */
public class Tool extends Producible {

  private final int efficiency;
  private final List<ResourceType> targets;

  @Contract(pure = true)
  public Tool(final int efficiency, final @NotNull List<ResourceType> targets) {
    this.efficiency = efficiency;
    this.targets = targets;
  }

  @Contract(" -> new")
  @Override
  public @NotNull @Unmodifiable Map<ResourceType, Integer> getCost() {
    return Map.of(ResourceType.WOOD, 1, ResourceType.STONE, 1);
  }

  public int getEfficiency() {
    return efficiency;
  }

  public List<ResourceType> getTargets() {
    return targets;
  }

  /**
   * Whether the tool can mine a certain resource.
   *
   * @param type The resource to check.
   * @return Whether the tool can mine the resource.
   */
  @Contract(pure = true)
  public boolean canMine(final ResourceType type) {
    return this.targets.contains(type);
  }
}
