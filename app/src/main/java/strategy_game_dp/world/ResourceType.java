package strategy_game_dp.world;

import org.jetbrains.annotations.Contract;

public enum ResourceType {
  WOOD(0.3, 50),
  STONE(0.3, 50),
  GOLD(0.3, 50),
  FOOD(0.3, 50);

  private final double spawnChance;
  private final int maxVeinSize;

  @Contract(pure = true)
  ResourceType(double spawnChance, int maxVeinSize) {
    this.spawnChance = spawnChance;
    this.maxVeinSize = maxVeinSize;
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
}
