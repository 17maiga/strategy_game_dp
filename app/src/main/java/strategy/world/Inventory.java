package strategy.world;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Singleton class that represents the inventory of the player.
 *
 * <p>The inventory is a map of {@link ResourceType}s to the amount of that resource the player has.
 */
public class Inventory {
  private static Inventory instance;
  private final Map<ResourceType, Integer> resources;

  @Contract(pure = true)
  public Inventory() {
    this.resources = new HashMap<>();
  }

  public static Inventory getInstance() {
    if (instance == null) {
      instance = new Inventory();
    }
    return instance;
  }

  public void add(final ResourceType type, final int amount) {
    resources.put(type, resources.getOrDefault(type, 0) + amount);
  }

  public void remove(final ResourceType type, final int amount) {
    resources.put(type, Math.max(resources.getOrDefault(type, 0) - amount, 0));
  }

  public void remove(final @NotNull Map<ResourceType, Integer> resources) {
    resources.forEach(this::remove);
  }

  public int get(final ResourceType type) {
    return resources.getOrDefault(type, 0);
  }

  public boolean contains(final ResourceType type, final int amount) {
    return resources.getOrDefault(type, 0) >= amount;
  }

  public boolean contains(final @NotNull Map<ResourceType, Integer> resources) {
    return resources.entrySet().stream().allMatch(entry -> contains(entry.getKey(), entry.getValue()));
  }
}
