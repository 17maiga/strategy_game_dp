package strategy.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import strategy.building.IBuilding;

/**
 * Singleton class that represents the inventory of the player.
 *
 * <p>The inventory is a map of {@link ResourceType}s to the amount of that resource the player has.
 */
public class Inventory {
  private final Map<ResourceType, Integer> resources;
  private final List<IBuilding> buildings;

  public Inventory() {
    this.resources = new HashMap<>();
    this.buildings = new ArrayList<>();
  }

  public void addResources(final ResourceType type, final int amount) {
    resources.put(type, resources.getOrDefault(type, 0) + amount);
  }

  public void removeResources(final ResourceType type, final int amount) {
    resources.put(type, Math.max(resources.getOrDefault(type, 0) - amount, 0));
  }

  public void removeResources(final @NotNull Map<ResourceType, Integer> resources) {
    resources.forEach(this::removeResources);
  }

  public int getResources(final ResourceType type) {
    return resources.getOrDefault(type, 0);
  }

  public boolean containsResources(final ResourceType type, final int amount) {
    return resources.getOrDefault(type, 0) >= amount;
  }

  public boolean containsResources(final @NotNull Map<ResourceType, Integer> resources) {
    return resources.entrySet().stream()
        .allMatch(entry -> containsResources(entry.getKey(), entry.getValue()));
  }

  public void addBuilding(final IBuilding building) {
    buildings.add(building);
  }

  public List<IBuilding> getBuildings() {
    return buildings;
  }
}
