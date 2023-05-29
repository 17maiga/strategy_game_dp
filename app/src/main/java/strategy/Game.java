package strategy;

import java.util.*;
import strategy.building.IBuilding;
import strategy.building.ToolBuilding;
import strategy.building.UnitBuilding;
import strategy.producible.Tool;
import strategy.producible.unit.Group;
import strategy.producible.unit.Unit;
import strategy.producible.unit.modifier.UnitModifier;
import strategy.world.Cell;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Game {
  private final WorldMap worldMap;
  private final Inventory inventory;

  public Game(final int width, final int height) {
    ArrayList<Unit> units = new ArrayList<>();
    Config.JOBS.forEach(
        (job, resources) -> {
          List<UnitModifier> modifiers = new ArrayList<>();
          for (Class<? extends UnitModifier> clazz : Config.UNIT_MODIFIERS) {
            try {
              modifiers.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          Unit unit =
              new Unit((int) (Math.random() * width), (int) (Math.random() * height), modifiers);
          unit.setTool(new Tool(1, resources));
          units.add(unit);
        });
    worldMap = new WorldMap(width, height);
    worldMap.insertUnits(units);
    inventory = new Inventory();
    inventory.addResources(ResourceType.FOOD, Config.INITIAL_FOOD_AMOUNT);
  }

  public void play() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Welcome to the game!\nUse 'help' to get help");
    boolean shouldQuit = false;
    render();
    while (!shouldQuit) {
      System.out.print("main> ");
      String input = scanner.nextLine();
      switch (input) {
        case "h", "help" -> helpMain();
        case "t", "turn" -> {
          shouldQuit = turn();
          if (!shouldQuit) {
            render();
          }
        }
        case "m", "manual" -> turnManual();
        case "p", "purchase" -> purchase();
        case "i", "inspect" -> inspect();
        case "r", "render" -> render();
        case "q", "quit" -> shouldQuit = quit();
        default -> System.out.println("Use 'help' to get help");
      }
    }
  }

  private boolean quit() {
    System.out.print("Are you sure you want to quit? (y/N)\nquit> ");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
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
      case RUNNING -> inventory
          .getBuildings()
          .forEach(building -> building.produce(worldMap, inventory));
    }
    return false;
  }

  private void separator() {
    for (int i = 0; i < 80; i++) {
      System.out.print('-');
    }
    System.out.print('\n');
  }

  public void render() {
    separator();
    System.out.print("Inventory: ");
    System.out.println(
        Arrays.stream(ResourceType.values())
            .map(type -> type + ": " + inventory.getResources(type))
            .reduce((a, b) -> a + ", " + b)
            .orElse(""));
    separator();
    List<IBuilding> buildings = inventory.getBuildings();
    if (!buildings.isEmpty()) {
      System.out.println("Buildings:");
      buildings.forEach(System.out::println);
      separator();
    }
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
    for (int i = 0; i < String.valueOf(worldMap.height()).length() + 2; i++) {
      System.out.print(' ');
    }
    for (int i = 0; i < worldMap.width(); i++) {
      StringBuilder iString = new StringBuilder(String.valueOf(i));
      while (iString.length() < cellWidth + 1) {
        iString.append(" ");
      }
      System.out.print(iString);
    }
    System.out.println("| Units:");
    for (int lineCount = 0; lineCount < worldMap.height(); lineCount++) {
      List<Cell> row = worldMap.cells().get(lineCount);
      StringBuilder lineCountBuilder = new StringBuilder(String.valueOf(lineCount));
      while (lineCountBuilder.length() < String.valueOf(worldMap.height()).length() + 1) {
        lineCountBuilder.append(" ");
      }
      System.out.print(lineCountBuilder);
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
        Unit unit = units.remove();
        System.out.print(unit);
        System.out.print(' ');
      }
      System.out.print('\n');
    }
    separator();
  }

  private void helpMain() {
    System.out.print(
        """
        List of commands available:
        h, help         Show this help menu
        t, turn         Play the next turn automatically for all units that have not played yet
        m, manual       Manually play a unit's turn
        p, purchase     Purchase a new building
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

  public void purchase() {
    System.out.println("List of possible factories:");
    System.out.println("u, unit         Unit Factory");
    System.out.println("t, tool         Tool Factory");
    System.out.print("Enter the type of factory you wish to purchase:\npurchase> ");
    Scanner scanner = new Scanner(System.in);
    String factory = scanner.nextLine();
    switch (factory) {
      case "u", "unit" -> {
        if (inventory.containsResources(IBuilding.getCost())) {
          System.out.println("Select the type of unit you wish to produce:");
          Config.JOBS.forEach((key, value) -> System.out.printf("  %s %s\n", key, value));
          System.out.print("purchase> ");
          String job = scanner.nextLine();
          if (!Config.JOBS.containsKey(job)) {
            System.out.println("Invalid unit");
            return;
          }
          List<ResourceType> resources = Config.JOBS.get(job);
          inventory.removeResources(IBuilding.getCost());
          inventory.addBuilding(new UnitBuilding(resources));
        } else {
          System.out.println("Not enough resources");
        }
      }
      case "t", "tool" -> {
        if (inventory.containsResources(IBuilding.getCost())) {
          System.out.println("Select the type of unit you wish to produce tools for:");
          Config.JOBS.forEach((key, value) -> System.out.printf("  %s (%s)\n", key, value));
          System.out.print("purchase> ");
          String tool = scanner.nextLine();
          if (!Config.JOBS.containsKey(tool)) {
            System.out.println("Invalid unit");
            return;
          }
          List<ResourceType> resources = Config.JOBS.get(tool);
          inventory.removeResources(IBuilding.getCost());
          inventory.addBuilding(new ToolBuilding(resources));
        } else {
          System.out.println("Not enough resources");
        }
      }
      default -> System.out.println("Invalid factory");
    }
  }

  public enum Status {
    RUNNING,
    WON,
    LOST
  }
}
