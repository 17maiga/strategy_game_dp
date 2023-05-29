package strategy.world;

import org.jetbrains.annotations.Contract;
import strategy.Config;

/**
 * Enum representing the different types of resources in the game.
 *
 * <p>Each resource has a spawn chance, a maximum vein size, and a symbol used to represent it on
 * the map.
 */
public enum ResourceType {
  WOOD(
      Config.RESOURCE_WOOD_SPAWN_CHANCE,
      Config.RESOURCE_WOOD_MAX_VEIN_SIZE),
  ROCK(
      Config.RESOURCE_ROCK_SPAWN_CHANCE,
      Config.RESOURCE_ROCK_MAX_VEIN_SIZE),
  GOLD(
      Config.RESOURCE_GOLD_SPAWN_CHANCE,
      Config.RESOURCE_GOLD_MAX_VEIN_SIZE),
  FOOD(
      Config.RESOURCE_FOOD_SPAWN_CHANCE,
      Config.RESOURCE_FOOD_MAX_VEIN_SIZE);

  private final double spawnChance;
  private final int maxVeinSize;
  private final char symbol;

  ResourceType(final double spawnChance, final int maxVeinSize) {
    this.spawnChance = spawnChance;
    this.maxVeinSize = maxVeinSize;
    symbol = name().charAt(0);
  }

  public static ResourceType getRandomType() {
    return values()[(int) (Math.random() * values().length)];
  }

  @Contract(pure = true)
  public double getSpawnChance() {
    return spawnChance;
  }

  @Contract(pure = true)
  public int getMaxVeinSize() {
    return maxVeinSize;
  }

  @Contract(pure = true)
  public char getSymbol() {
    return symbol;
  }
}
