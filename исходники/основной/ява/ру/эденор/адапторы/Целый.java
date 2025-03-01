package ру.эденор.адапторы;

public class Целый {
  public static int вычитатьИзСтроки(String строка) {
    return Integer.parseInt(строка);
  }

  public static Long вычитатьДлииный(String строка) {
    if (строка == null) {
      return null;
    }
    try {

      return Long.parseLong(строка);
    } catch (NumberFormatException e) {
      return null;
    }
  }
}
