package strategy;

import java.util.*;

import org.jetbrains.annotations.Contract;
import strategy.producible.Tool;
import strategy.producible.unit.Group;
import strategy.producible.unit.Unit;
import strategy.world.Cell;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Game {
  private static Game instance;

  public Game(final int width, final int height) {
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

  public static void createInstance(final int width, final int height) {
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
  }

  private boolean turn() {
    switch (WorldMap.getInstance().turn()) {
      case WON -> {
        System.out.println("You won!");
        return true;
      }
      case LOST -> {
        System.out.println("You lost!");
        return true;
      }
      case RUNNING -> System.out.println("Turn played");
    }
    return false;
  }

  public void play() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Use 'help' to get help");
    boolean shouldQuit = false;
    while (!shouldQuit) {
      render();
      boolean shouldTurn = false;
      while (!shouldTurn) {
        System.out.print("> ");
        String input = scanner.nextLine();
        switch (input) {
          case "h", "help" -> helpMenu();
          case "t", "turn", "" -> {
            shouldTurn = true;
            shouldQuit = turn();
          }
          case "i", "inspect" -> inspect();
          case "r", "render" -> render();
          case "q", "quit" -> {
            shouldQuit = true;
            shouldTurn = true;
          }
          default -> System.out.println("Use 'help' to get help");
        }
      }
    }
  }

  private void helpMenu() {
    System.out.print(
        """
        List of commands:
        h, help         Show this help menu
        t, turn         Play the next turn
        i, inspect      Inspect the contents of a specific tile
        r, render       Render the game map again
        q, quit         Quit the game
        """);
  }

  private void inspect() {
    Scanner scanner = new Scanner(System.in);
    System.out.print(
        "Enter the coordinates of the cell you wish to inspect (x and y separated by a space): ");
    String[] coordinates = scanner.nextLine().split(" ");
    if (coordinates.length != 2) {
      System.out.println("Invalid coordinates");
    }
    try {
      int x = Integer.parseInt(coordinates[0]);
      int y = Integer.parseInt(coordinates[1]);
      Cell cell = WorldMap.getInstance().getCell(x, y);
      System.out.println("Cell at (" + cell.getX() + ", " + cell.getY() + "):");
      System.out.println(
          "  Resources: "
              + (cell.getAmount() > 0 ? cell.getAmount() + " " + cell.getType() : "None"));
      Unit unit = cell.getUnit();
      if (unit != null) {
        if (unit instanceof Group) {
          System.out.println("  Unit: Group");
          ((Group) unit)
              .getUnits()
              .forEach(
                  u ->
                      System.out.println(
                          "    - "
                              + u.getTargets().stream()
                                  .map(ResourceType::getJob)
                                  .reduce((a, b) -> a + ", " + b)
                                  .orElse("Unemployed")));
        } else {
          System.out.println(
              "  Unit: "
                  + unit.getTargets().stream()
                      .map(ResourceType::getJob)
                      .reduce((a, b) -> a + ", " + b)
                      .orElse("Unemployed"));
        }
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid coordinates");
    }
  }

  public enum Status {
    RUNNING,
    WON,
    LOST
  }
}
