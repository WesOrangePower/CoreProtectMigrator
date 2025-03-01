package ру.эденор.адапторы;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SuppressWarnings("NonAsciiCharacters")
public class Строки {
  public static String соединить(String... строки) {
    StringBuilder результат = new StringBuilder();
    for (String строка : строки) {
      результат.append(строка);
    }
    return результат.toString();
  }

  public static String соединитьЧерез(
      CharSequence разделитель, Iterable<? extends CharSequence> строки) {
    return String.join(разделитель, строки);
  }

  public static boolean начинаетсяС(String строка, String префикс) {
    return строка.startsWith(префикс);
  }

  public static boolean заканчиваетсяНа(String строка, String суффикс) {
    return строка.endsWith(суффикс);
  }

  public static String заменить(String строка, String что, String на) {
    return строка.replace(что, на);
  }

  public static String обрезать(String строка, int начало, int конец) {
    return строка.substring(начало, конец);
  }

  public static String обрезать(String строка, int начало) {
    return строка.substring(начало);
  }

  public static boolean равны(String строка, String другая) {
    return строка.equals(другая);
  }

  public static boolean содержит(String строка, String подстрока) {
    return строка.contains(подстрока);
  }

  public static String закодироватьВСтепень64(String строка) {
    return Base64.getEncoder().encodeToString(строка.getBytes(StandardCharsets.UTF_8));
  }

  public static String закодироватьВСтепень64(byte[] байты) {
    return Base64.getEncoder().encodeToString(байты);
  }

  public static String заменитьРегулярнымВырожением(String заменить, String что, String на) {
    return заменить.replaceAll(что, на);
  }

  public static String отформатировать(String строка, Object... аргументы) {
    return String.format(строка, аргументы);
  }
}
