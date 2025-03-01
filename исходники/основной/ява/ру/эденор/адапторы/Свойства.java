package ру.эденор.адапторы;

import java.util.Properties;

public class Свойства {

  private final Properties свойства;

  public Свойства(Properties свойства) {
    this.свойства = свойства;
  }

  public Свойства() {
    this.свойства = new Properties();
  }

  public String получитьСвойство(String название) {
    return свойства.getProperty(название);
  }

  public String получитьСвойство(String название, String поУмолчанию) {
    return свойства.getProperty(название, поУмолчанию);
  }

  public void установитьСвойство(String название, String значение) {
    свойства.setProperty(название, значение);
  }
}
