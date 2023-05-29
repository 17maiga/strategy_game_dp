package strategy;

public class App {
  private static final int HEIGHT = 8;
  private static final int WIDTH = 8;

  public static void main(String[] args) {
    new Game(WIDTH, HEIGHT).play();
  }
}
