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
public record Tool(int efficiency, List<ResourceType> targets) {

  public static final Map<String, List<ResourceType>> jobs =
      Map.of(
          "Miner",
          List.of(ResourceType.ROCK, ResourceType.GOLD),
          "Lumberjack",
          List.of(ResourceType.WOOD),
          "Farmer",
          List.of(ResourceType.FOOD));

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
