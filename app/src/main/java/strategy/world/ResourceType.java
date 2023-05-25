package strategy.world;

import org.jetbrains.annotations.Contract;

/**
 * Enum representing the different types of resources in the game.
 *
 * <p>Each resource has a spawn chance, a maximum vein size, and a symbol used to represent it on
 * the map.
 */
public enum ResourceType {
  WOOD(0.3, 50),
  STONE(0.3, 50),
  GOLD(0.3, 50),
  FOOD(0.3, 50);

  private final double spawnChance;
  private final int maxVeinSize;
  private final char symbol;

  @Contract(pure = true)
  ResourceType(double spawnChance, int maxVeinSize) {
    this.spawnChance = spawnChance;
    this.maxVeinSize = maxVeinSize;
    symbol = name().charAt(0);
  }

  @Contract(pure = true)
  public static ResourceType getRandomType() {
    return WOOD;
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
