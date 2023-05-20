package strategy;

import java.util.*;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.ResourceType;
import strategy.world.WorldMap;

public class Game {
  private final WorldMap worldMap;
  private final List<Unit> units;
  private final Inventory inventory;

  public Game() {
    worldMap = WorldMap.getInstance(8, 8);
    units =
        List.of(
            new Unit(new HashMap<>(), 0, 0, new ArrayList<>()),
            new Unit(new HashMap<>(), 1, 1, new ArrayList<>()),
            new Unit(new HashMap<>(), 2, 2, new ArrayList<>()));
    worldMap.insertUnits(units);
    inventory = Inventory.getInstance();
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
                char contents = ' ';
                if (cell.getAmount() > 0) contents = cell.getType().getSymbol();
                if (cell.getUnit() != null) {
                  units.add(cell.getUnit());
                  contents = 'U';
                }
                System.out.printf(" %c ", contents);
              });
      System.out.print(" | ");
      while (!units.isEmpty()) {
        Unit unit = units.poll();
        System.out.printf(" (%d %d) ", unit.getX(), unit.getY());
      }
      System.out.print('\n');
    }
  }
}
