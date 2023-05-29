package strategy.producible;

import java.util.List;
import org.jetbrains.annotations.Contract;
import strategy.world.ResourceType;

/**
 * <b>Class representing a tool.</b>
 *
 * <p>Tools are used by units to mine resources. They have an efficiency and a list of resources
 * they can mine. They can be produced by buildings.
 */
public record Tool(int efficiency, List<ResourceType> targets) {
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
