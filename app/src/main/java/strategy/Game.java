package strategy;

import java.util.*;

import org.jetbrains.annotations.Contract;
import strategy.producible.Tool;
import strategy.producible.unit.Group;
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
    units = new ArrayList<>();
    for (ResourceType type : ResourceType.values()) {
      Unit unit =
          new Unit(
              (int) Math.floor(Math.random() * width),
              (int) Math.floor(Math.random() * height),
              new ArrayList<>());
      units.add(unit);
      unit.setTool(new Tool(1, List.of(type)));
    }
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
    for (int i = 0; i < 120; i++) {
      System.out.print('-');
    }
    System.out.print('\n');
    System.out.print("Inventory: ");
    System.out.println(
        Arrays.stream(ResourceType.values())
            .map(type -> type + ": " + inventory.get(type))
            .reduce((a, b) -> a + ", " + b)
            .orElse(""));
    for (int i = 0; i < 120; i++) {
      System.out.print('-');
    }
    System.out.print('\n');

    Queue<Unit> units = new ArrayDeque<>();
    int cellWidth =
        String.valueOf(
                    Arrays.stream(ResourceType.values())
                        .mapToInt(ResourceType::getMaxVeinSize)
                        .max()
                        .orElse(0))
                .length()
            + 2;

    for (int lineCount = 0; lineCount < worldMap.height(); lineCount++) {
      worldMap
          .cells()
          .get(lineCount)
          .forEach(
              cell -> {
                StringBuilder displayBuilder = new StringBuilder();
                if (cell.getUnit() != null) {
                  units.add(cell.getUnit());
                  displayBuilder.append('[');
                } else {
                  displayBuilder.append(' ');
                }
                if (cell.getAmount() > 0) {
                  displayBuilder.append(cell.getType().getSymbol());
                  displayBuilder.append(cell.getAmount());
                }
                while (displayBuilder.length() < cellWidth) {
                  displayBuilder.append(' ');
                }
                if (cell.getUnit() != null) {
                  displayBuilder.append(']');
                } else {
                  displayBuilder.append(' ');
                }
                System.out.print(displayBuilder.toString());
              });
      System.out.print(" | ");
      while (!units.isEmpty()) {
        Unit unit = units.poll();
        String jobs = "Unemployed";
        if (unit instanceof Group) {
          jobs = "Group";
        } else if (unit.getTool() != null) {
          jobs =
              unit.getTool().getTargets().stream()
                  .map(ResourceType::getJob)
                  .reduce((a, b) -> a + " - " + b)
                  .orElse("");
        }
        System.out.printf(" [%s | %d:%d] ", jobs, unit.getX(), unit.getY());
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
