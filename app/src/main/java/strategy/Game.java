package strategy;

import java.util.*;
import strategy.building.UnitBuilding;
import strategy.producible.Tool;
import strategy.producible.unit.Group;
import strategy.producible.unit.Unit;
import strategy.world.Cell;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Game {
  private final WorldMap worldMap;
  private final Inventory inventory;

  public Game(final int width, final int height) {
    ArrayList<Unit> units = new ArrayList<>();
    List.of(ResourceType.values())
        .forEach(
            type -> {
              Unit unit =
                  new Unit(
                      (int) (Math.random() * width),
                      (int) (Math.random() * height),
                      new ArrayList<>());
              unit.setTool(new Tool(1, List.of(type)));
              units.add(unit);
            });
    worldMap = new WorldMap(width, height);
    worldMap.insertUnits(units);
    inventory = new Inventory();
    inventory.addResources(ResourceType.FOOD, 100);
    inventory.addBuilding(new UnitBuilding());
  }

  public void render() {
    for (int i = 0; i < 120; i++) {
      System.out.print('-');
    }
    System.out.print('\n');
    System.out.print("Inventory: ");
    System.out.println(
        Arrays.stream(ResourceType.values())
            .map(type -> type + ": " + inventory.getResources(type))
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

    // Print column numbers
    System.out.print("   ");
    for (int i = 0; i < worldMap.width(); i++) {
      System.out.print(i);
      for (int j = 0; j < cellWidth; j++) {
        System.out.print(' ');
      }
    }
    System.out.println("|");
    for (int lineCount = 0; lineCount < worldMap.height(); lineCount++) {
      List<Cell> row = worldMap.cells().get(lineCount);
      System.out.print(lineCount + " ");
      row.forEach(
          cell -> {
            StringBuilder displayBuilder = new StringBuilder();
            if (cell.getUnit() != null) {
              units.add(cell.getUnit());
              if (cell.getUnit().hasPlayed()) {
                displayBuilder.append('{');
              } else if (cell.getUnit().canMine()) {
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
              if (cell.getUnit().hasPlayed()) {
                displayBuilder.append('}');
              } else if (cell.getUnit().canMine()) {
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
        String format =
            unit.hasPlayed()
                ? " { (%d %d) %s } "
                : unit.canMine() ? " [ (%d %d) %s ] " : " ( (%d %d) %s ) ";
        System.out.printf(format, unit.getX(), unit.getY(), unit.getJob());
      }
      System.out.print('\n');
    }
  }

  private boolean turn() {
    switch (worldMap.turn(inventory)) {
      case WON -> {
        System.out.println("You won!");
        return true;
      }
      case LOST -> {
        System.out.println("You lost!");
        return true;
      }
      case RUNNING -> {
        inventory.getBuildings().forEach(building -> building.produce(worldMap, inventory));
        System.out.println("Turn played");
      }
    }
    return false;
  }

  public void play() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the game!\nUse 'help' to get help");
    boolean shouldQuit = false;
    while (!shouldQuit) {
      render();
      boolean shouldTurn = false;
      while (!shouldTurn) {
        System.out.print("main> ");
        String input = scanner.nextLine();
        switch (input) {
          case "h", "help" -> helpMain();
          case "t", "turn", "" -> {
            shouldTurn = true;
            shouldQuit = turn();
          }
          case "m", "manual" -> turnManual();
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

  private void helpMain() {
    System.out.print(
        """
        List of commands available:
        h, help         Show this help menu
        t, turn         Play the next turn automatically for all units that have not played yet
        m, manual       Manually play a unit's turn
        i, inspect      Inspect the contents of a specific tile
        r, render       Render the game map again
        q, quit         Quit the game
        """);
  }

  private void helpManual() {
    System.out.print(
        """
        List of commands available:
        h, help         Show this help menu
        m, move         Move the unit to a specific tile
        i, inspect      Inspect the contents of a specific tile
        r, render       Render the game map again
        e, extract      Extract resources from the current tile
        c, cancel       Cancel the current unit's turn
        """);
  }

  private void inspect() {
    Scanner scanner = new Scanner(System.in);
    System.out.print(
        "Enter the coordinates of the cell you wish to inspect (x and y separated by a space):\ninspect> ");
    String[] coordinates = scanner.nextLine().split(" ");
    if (coordinates.length != 2) {
      System.out.println("Invalid coordinates");
    }
    try {
      int x = Integer.parseInt(coordinates[0]);
      int y = Integer.parseInt(coordinates[1]);
      Cell cell = worldMap.getCell(x, y);
      System.out.printf(
          "Cell at (%d %d):\n  Resources: %s\n",
          cell.getX(),
          cell.getY(),
          (cell.getAmount() > 0 ? cell.getAmount() + " " + cell.getType() : "None"));
      Unit unit = cell.getUnit();
      if (unit != null) {
        if (unit instanceof Group) {
          System.out.println("  Unit: Group");
          ((Group) unit).getUnits().forEach(u -> System.out.printf("    - %s\n", u.getJob()));
        } else {
          System.out.printf("  Unit: %s\n", unit.getJob());
        }
      }
    } catch (NumberFormatException e) {
      System.out.println("Invalid coordinates");
    }
  }

  public void turnManual() {
    List<Unit> units = worldMap.getUnits().stream().filter(u -> !u.hasPlayed()).toList();
    if (units.isEmpty()) {
      System.out.println("No units left to play. Use 'turn' to go to the next turn.");
      return;
    }
    System.out.println("List of possible units:");
    for (int i = 0; i < units.size(); i++) {
      Unit unit = units.get(i);
      System.out.printf("  %d: (%d, %d) %s\n", i, unit.getX(), unit.getY(), unit.getJob());
    }
    System.out.print("Enter the number of the unit you wish to play:\nmanual> ");
    Scanner scanner = new Scanner(System.in);
    int index;
    try {
      index = Integer.parseInt(scanner.nextLine());
    } catch (NumberFormatException e) {
      System.out.println("Invalid number");
      return;
    }
    if (index < 0 || index >= units.size()) {
      System.out.println("Invalid number");
      return;
    }
    Unit unit = units.get(index);
    System.out.printf(
        "Playing unit %d: (%d %d) %s\n", index, unit.getX(), unit.getY(), unit.getJob());
    helpManual();
    boolean shouldLoop = true;
    while (shouldLoop) {
      System.out.print("manual> ");
      String action = scanner.nextLine();
      switch (action) {
        case "m", "move" -> {
          unit.eat(inventory);
          System.out.print("Enter the coordinates of the cell you wish to move to: ");
          String[] coordinates = scanner.nextLine().split(" ");
          if (coordinates.length != 2) {
            System.out.println("Invalid coordinates");
            return;
          }
          try {
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);
            unit.move(x, y, worldMap);
          } catch (NumberFormatException e) {
            System.out.println("Invalid coordinates");
          }
          shouldLoop = false;
          unit.setHasPlayed(true);
        }
        case "e", "extract" -> {
          unit.eat(inventory);
          if (unit.mine(worldMap, inventory)) {
            System.out.println("Mined successfully.");
          } else {
            System.out.println("Could not mine.");
          }
          shouldLoop = false;
          unit.setHasPlayed(true);
        }
        case "r", "render" -> render();
        case "i", "inspect" -> inspect();
        case "c", "cancel" -> shouldLoop = false;
        case "h", "help" -> helpManual();
        default -> System.out.println("Invalid action");
      }
    }
  }

  public enum Status {
    RUNNING,
    WON,
    LOST
  }
}
