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

  public Game(int width, int height) {
    Unit u1 = new Unit(0, 0, new ArrayList<>());
    u1.setTool(new Tool(2, List.of(ResourceType.WOOD, ResourceType.ROCK)));
    Unit u2 = new Unit(0, 0, new ArrayList<>());
    u2.setTool(new Tool(2, List.of(ResourceType.WOOD, ResourceType.FOOD)));
    WorldMap.getInstance(width, height).insertUnits(List.of(u2, u1));
    Inventory.getInstance().add(ResourceType.FOOD, 100);
  }

  @Contract(pure = true)
  public static Game getInstance() {
    if (instance == null) {
      throw new UnsupportedOperationException();
    }
    return instance;
  }

  public static void createInstance(int width, int height) {
    if (instance == null) {
      instance = new Game(width, height);
    }
  }

  public void render() {
    for (int i = 0; i < 120; i++) {
      System.out.print('-');
    }
    System.out.print('\n');
    System.out.print("Inventory: ");
    System.out.println(
        Arrays.stream(ResourceType.values())
            .map(type -> type + ": " + Inventory.getInstance().get(type))
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
            + 3;

    for (int lineCount = 0; lineCount < WorldMap.getInstance().height(); lineCount++) {
      WorldMap.getInstance()
          .cells()
          .get(lineCount)
          .forEach(
              cell -> {
                StringBuilder displayBuilder = new StringBuilder();
                if (cell.getUnit() != null) {
                  units.add(cell.getUnit());
                  if (cell.getUnit().canMine()) {
                    displayBuilder.append('[');
                  } else {
                    displayBuilder.append('(');
                  }
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
                  if (cell.getUnit().canMine()) {
                    displayBuilder.append(']');
                  } else {
                    displayBuilder.append(')');
                  }
                } else {
                  displayBuilder.append(' ');
                }
                System.out.print(displayBuilder);
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
        String format = unit.canMine() ? " [%s | %d:%d] " : " (%s | %d:%d) ";
        System.out.printf(format, jobs, unit.getX(), unit.getY());
      }
      System.out.print('\n');
    }
    WorldMap.getInstance()
        .getUnits()
        .forEach(
            unit -> System.out.println(unit.getX() + " " + unit.getY() + " " + unit.getTargets()));
  }

  public Status turn() {
    return WorldMap.getInstance().turn();
  }

  public enum Status {
    RUNNING,
    WON,
    LOST
  }
}
