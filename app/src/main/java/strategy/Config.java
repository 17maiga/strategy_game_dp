package strategy;

import strategy.producible.unit.modifier.ExpertModifier;
import strategy.producible.unit.modifier.RiderModifier;
import strategy.producible.unit.modifier.UnitModifier;
import strategy.world.ResourceType;

import java.util.List;
import java.util.Map;

public class Config {
  public static final int HEIGHT = 20;
  public static final int WIDTH = 20;
  public static final int INITIAL_FOOD_AMOUNT = 30;

  public static final List<Class<? extends UnitModifier>> UNIT_MODIFIERS =
      List.of(ExpertModifier.class, RiderModifier.class);

  public static final Map<ResourceType, Integer> BUILDING_COST =
      Map.of(
          ResourceType.ROCK,
          10,
          ResourceType.WOOD,
          10,
          ResourceType.GOLD,
          10,
          ResourceType.FOOD,
          10);
  public static final int BUILDING_PRODUCTION_TIME = 2;
  public static final Map<ResourceType, Integer> UNIT_COST =
      Map.of(ResourceType.FOOD, 1, ResourceType.GOLD, 1);
  public static final int UNIT_BASE_SPEED = 3;
  public static final int UNIT_BASE_HUNGER = 1;

  public static final Map<ResourceType, Integer> TOOL_COST =
      Map.of(ResourceType.ROCK, 1, ResourceType.WOOD, 1);
  public static final int TOOL_BASE_EFFICIENCY = 1;
  public static final Map<String, List<ResourceType>> JOBS =
      Map.of(
          "Miner",
          List.of(ResourceType.ROCK, ResourceType.GOLD),
          "Lumberjack",
          List.of(ResourceType.WOOD),
          "Farmer",
          List.of(ResourceType.FOOD));

  public static final double GROUP_EFFICIENCY_MULTIPLIER = 1.1;

  public static final int EXPERT_XP_THRESHOLD = 10;
  public static final int EXPERT_DEACTIVATE_COUNTDOWN = 5;
  public static final double EXPERT_SPEED_MULTIPLIER = 1.5;
  public static final int EXPERT_EFFICIENCY_MULTIPLIER = 2;
  public static final int EXPERT_HUNGER_MULTIPLIER = 2;

  public static final int RIDER_SPEED_MULTIPLIER = 2;
  public static final int RIDER_HUNGER_INCREASE = 3;

  public static final float RESOURCE_WOOD_SPAWN_CHANCE = 0.3f;
  public static final float RESOURCE_ROCK_SPAWN_CHANCE = 0.3f;
  public static final float RESOURCE_GOLD_SPAWN_CHANCE = 0.3f;
  public static final float RESOURCE_FOOD_SPAWN_CHANCE = 0.3f;

  public static final int RESOURCE_WOOD_MAX_VEIN_SIZE = 3;
  public static final int RESOURCE_ROCK_MAX_VEIN_SIZE = 3;
  public static final int RESOURCE_GOLD_MAX_VEIN_SIZE = 3;
  public static final int RESOURCE_FOOD_MAX_VEIN_SIZE = 3;
}
