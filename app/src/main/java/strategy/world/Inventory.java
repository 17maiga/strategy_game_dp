package strategy.world;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
  private final Map<ResourceType, Integer> resources;

  private static Inventory instance;

  @Contract(pure = true)
  public Inventory() {
    this.resources = new HashMap<>();
  }

  public void add(final ResourceType type, final int amount) {
    resources.put(type, resources.getOrDefault(type, 0) + amount);
  }

  public void remove(final ResourceType type, final int amount) {
    resources.put(type, resources.getOrDefault(type, 0) - amount);
  }

  public int get(final ResourceType type) {
    return resources.getOrDefault(type, 0);
  }

  public static Inventory getInstance() {
    if (instance == null) {
      instance = new Inventory();
    }
    return instance;
  }
}
