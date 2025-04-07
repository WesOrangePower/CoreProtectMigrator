package ру.эденор.адапторы;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Файлы {
  public static Список<String> прочитатьВсеСтроки(Path путь) throws ИсключениеВводаВывода {
    try {
      return СписокНаОсновеМассива.изЯвавогоСписка(Files.readAllLines(путь));
    } catch (IOException и) {
      throw new ИсключениеВводаВывода(и);
    }
  }

  public static boolean существует(Path путь) {
    return Files.exists(путь);
  }

  public static Path путь(String путь) {
    return Paths.get(путь);
  }

  public static void дописать(Path путь, String строка) throws ИсключениеВводаВывода {
    try {
      Files.writeString(путь, строка, StandardOpenOption.APPEND);
    } catch (IOException и) {
      throw new ИсключениеВводаВывода(и);
    }
  }
}
