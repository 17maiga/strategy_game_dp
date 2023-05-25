package strategy.building;

public interface IBuilding<T> {
  /**
   * @return the producible object.
   */
  T produce();
}
