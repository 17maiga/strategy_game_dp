package strategy;

import java.util.*;

import org.jetbrains.annotations.Contract;
import strategy.producible.Tool;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Game {
  private static Game instance;
  private final WorldMap worldMap;
  private final List<Unit> units;
  private final Inventory inventory;

  public Game(int width, int height) {
    worldMap = WorldMap.getInstance(width, height);
    units =
        List.of(
            new Unit(
                (int) Math.floor(Math.random() * width),
                (int) Math.floor(Math.random() * height),
                new ArrayList<>()));
    units.forEach(unit -> unit.setTool(new Tool(1, List.of(ResourceType.FOOD))));
    worldMap.insertUnits(units);
    inventory = Inventory.getInstance();
    inventory.add(ResourceType.FOOD, 100);
  }

  @Contract(pure = true)
  public static Game getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException();
    }
    return instance;
  }

  public static Game getInstance(int width, int height) {
    if (instance == null) {
      instance = new Game(width, height);
    }
    return instance;
  }

  public void render() {
    for (int i = 0; i < 120; i++) System.out.print('-');
    System.out.print('\n');
    System.out.printf(
        "Inventory | FOOD: %d, GOLD: %d, STONE: %d, WOOD: %d\n",
        inventory.get(ResourceType.FOOD),
        inventory.get(ResourceType.GOLD),
        inventory.get(ResourceType.STONE),
        inventory.get(ResourceType.WOOD));
    for (int i = 0; i < 120; i++) System.out.print('-');
    System.out.print('\n');

    Queue<Unit> units = new ArrayDeque<>();
    for (int lineCount = 0; lineCount < worldMap.height(); lineCount++) {
      worldMap
          .cells()
          .get(lineCount)
          .forEach(
              cell -> {
                char resources = ' ';
                String format = " %c ";
                if (cell.getAmount() > 0) resources = cell.getType().getSymbol();
                if (cell.getUnit() != null) {
                  units.add(cell.getUnit());
                  format = "[%c]";
                }
                System.out.printf(format, resources);
              });
      System.out.print(" | ");
      while (!units.isEmpty()) {
        Unit unit = units.poll();
        System.out.printf(" [X:%d, Y:%d, T:%d] ", unit.getX(), unit.getY(), unit.getEfficiency());
      }
      System.out.print('\n');
    }
  }

  public List<Unit> getUnits() {
    return units;
  }

  public WorldMap getWorldMap() {
    return worldMap;
  }

  public Inventory getInventory() {
    return inventory;
  }

  public boolean turn() {
    units.forEach(Unit::turn);
    return worldMap.isWin();
  }
}
