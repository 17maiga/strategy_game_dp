package strategy.world;

import org.jetbrains.annotations.Contract;

/**
 * Enum representing the different types of resources in the game.
 *
 * <p>Each resource has a spawn chance, a maximum vein size, and a symbol used to represent it on
 * the map.
 */
public enum ResourceType {
  WOOD(0.3, 3, "Woodcutter"),
  ROCK(0.3, 3, "Rock Miner"),
  GOLD(0.3, 3, "Gold Miner"),
  FOOD(0.3, 3, "Farmer");

  private final double spawnChance;
  private final int maxVeinSize;
  private final char symbol;
  private final String job;

  @Contract(pure = true)
  ResourceType(double spawnChance, int maxVeinSize, String job) {
    this.spawnChance = spawnChance;
    this.maxVeinSize = maxVeinSize;
    symbol = name().charAt(0);
    this.job = job;
  }

  @Contract(pure = true)
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

  @Contract(pure = true)
  public String getJob() {
    return job;
  }
}
