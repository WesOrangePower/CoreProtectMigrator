package ру.эденор;

public class ИсключениеНелегальногоАргумента extends IllegalArgumentException {
  public ИсключениеНелегальногоАргумента(String message) {
    super(message);
  }
}
