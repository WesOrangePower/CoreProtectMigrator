package ру.эденор.адапторы;

import java.io.InputStream;
import java.io.PrintStream;

public class Система {
  public static InputStream ввод = System.in;
  public static PrintStream вывод = System.out;
  public static PrintStream ошибка = System.err;

  public static long времяСейчас() {
    return System.currentTimeMillis();
  }

  public static void выйти(int код) {
    System.exit(код);
  }

  public static void печать(String строка) {
    вывод.print(строка);
  }

  public static void печатьф(String формат, Object... аргументы) {
    вывод.printf(формат, аргументы);
  }

  public static void печатьстр(String строка) {
    вывод.println(строка);
  }

  public static void ошибка(String строка) {
    ошибка.print(строка);
  }

  public static void ошибкаф(String формат, Object... аргументы) {
    ошибка.printf(формат, аргументы);
  }

  public static void ошибкастр(String строка) {
    ошибка.println(строка);
  }
}
