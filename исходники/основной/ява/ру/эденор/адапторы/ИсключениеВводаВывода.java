package ру.эденор.адапторы;

import java.io.IOException;

public class ИсключениеВводаВывода extends IOException {

  private final IOException исключение;

  public ИсключениеВводаВывода(IOException исключение) {
    super(исключение);
    this.исключение = исключение;
    setStackTrace(исключение.getStackTrace());
  }
}
