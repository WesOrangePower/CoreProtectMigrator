package ру.эденор;

import org.json.JSONException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

@SuppressWarnings("NonAsciiCharacters")
public class Мигратор {

  private Подключение подклМарии;
  private Подключение подклЛайта;
  private Properties окружение;

  private static final long времяНачала = Система.времяСейчас();

  public static void main(String[] args) throws SQLException, IOException, JSONException {
    Мигратор мигратор = new Мигратор();
    мигратор.пуск();
  }

  public Мигратор() throws IOException {
    подгрузитьОкружение();
  }

  private void подгрузитьОкружение() throws IOException {
    окружение = new Properties();
    var путьКОкружению = Paths.get(".окр");
    if (!Files.exists(путьКОкружению)) {
      Система.ошибкастр(".окр файл не найден.");
      Система.ошибкастр("Пожалуйста, создайте его на основе _окр файла и загрузите его командой \"source .окр\".");
      Система.ошибкастр("Для получения дополнительной информации ознакомьтесь с файлом ПРОЧИТАТЬ.md.");
      Система.выйти(1);
    }
    var строки = Files.readAllLines(путьКОкружению);
    for (var строка : строки) {
      if (!строка.startsWith("#") && строка.contains("=")) {
        var части = строка.split("=");
        окружение.setProperty(части[0].trim(), части[1].trim());
      }
    }
  }

  private Подключение подключениеМарии() throws ИсключениеЯСЗ {
    if (подклМарии == null) {
      try {
       Класс.поИмени("org.mariadb.jdbc.Driver");
      } catch (ИсключениеКлассНеНайден e) {
        Система.ошибка("Драйвер MariaDB не найден.");
        Система.выйти(1);
      }
      var ссылка = "jdbc:mariadb://" + окружение.getProperty("MYSQL_HOST") + ":" + окружение.getProperty("MYSQL_PORT") + "/" + окружение.getProperty("MYSQL_DATABASE");
      подклМарии = УправляющийДрайверами.подключиться(ссылка, окружение.getProperty("MYSQL_USER"), окружение.getProperty("MYSQL_PASS"));
    }
    return подклМарии;
  }

  private void очиститьТаблицуМарии(String таблица) throws ИсключениеЯСЗ {
    Система.печатьф("Вычищаю %s... ", таблица);
    try (Положение положение = подключениеМарии().создатьПоложение()) {
      положение.исполнить("TRUNCATE TABLE `" + таблица + "`");
      положение.исполнить("ALTER TABLE `" + таблица + "` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
      if (Строки.равны(таблица, "co_item")) {
        положение.исполнить("ALTER TABLE `co_item` CHANGE `data` `data` MEDIUMBLOB NULL;");
      }
    }
    Система.печатьстр("готово!");
  }

  static Integer ошибок = 0;
  static Set<String> таблицыСОшибками = new HashSet<>();

  private void вставитьДанныеВМарию(String таблица, String колонки, String значения) throws ИсключениеЯСЗ {
    try (Положение положение = подключениеМарии().создатьПоложение()) {
      подключениеМарии().выключитьАвтоПрименение(); // Начало транзакции
      var запрос = "INSERT INTO " + таблица + " (" + колонки + ") VALUES " + значения;
      try {
        положение.исполнить(запрос);
      } catch (ИсключениеЯСЗ и) {
        if (и.кодОшибки() == 42) {
          if (Строки.содержит(и.сообщение(), "Data too long")) {
            таблицыСОшибками.add(таблица);
            ошибок++;
          }
        }
        throw и;
      }
      подключениеМарии().применить(); // Применяем транзакцию
    } finally {
      подключениеМарии().включитьАвтоПрименение(); // Удостоверимся, что автоприменение сброшено
    }
  }


  private Подключение подключениеЛайта() throws ИсключениеЯСЗ {
    if (подклЛайта == null) {
      try {
        Класс.поИмени("org.sqlite.JDBC");
      } catch (ИсключениеКлассНеНайден и) {
        Система.ошибкастр("SQLite JDBC driver not found.");
        Система.выйти(1);
      }
      var ссылка = "jdbc:sqlite:" + окружение.getProperty("SQLITE_DATABASE");
      подклЛайта = УправляющийДрайверами.подключиться(ссылка);
    }
    return подклЛайта;
  }

  private List<String> таблицыЛайта() throws ИсключениеЯСЗ {
    List<String> таблицы = new ArrayList<>();
    try (Положение положение = подключениеЛайта().создатьПоложение();
         ОтветныйНабор набор = положение.исполнитьЗапрос("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;")) {
      while (набор.следующий()) {
        таблицы.add(набор.достатьСтроку("name"));
      }
    }
    return таблицы;
  }

  private int количествоСтрокЛайта(String таблица) throws ИсключениеЯСЗ {
    try (Положение положение = подключениеЛайта().создатьПоложение();
         ОтветныйНабор набор = положение.исполнитьЗапрос("SELECT COUNT(*) FROM " + таблица)) {
      набор.следующий();
      return набор.достатьЦел(1);
    }
  }

  private String колонкиТаблицыЛайта(String таблица) throws SQLException {
    try (ПодготовленноеПоложение положение = подключениеЛайта().подготовленныйЗапрос("SELECT * FROM " + таблица + " LIMIT 1")) {
      МетаДанныеОтветногоНабора метаДанные = положение.метаДанные();
      var колонки = new ArrayList<String>();
      for (int i = 1; i <= метаДанные.количествоКолонок(); i++) {
        var имяКолонки = метаДанные.имяКолонки(i);
        колонки.add(Строки.равны(имяКолонки, "id") ? "rowid" : имяКолонки);
      }
      return Строки.соединитьЧерез(",", колонки);
    }
  }


  private List<Map<String, Object>> заказатьЛайт(String таблица, int отлад) throws SQLException {
    List<Map<String, Object>> ряды = new ArrayList<>();
    String ясз = Строки.отформатировать("SELECT * FROM %s LIMIT %s OFFSET %s", таблица, окружение.getProperty("OFFSET"), отлад);
    try (Положение положение = подключениеЛайта().создатьПоложение();
         ОтветныйНабор набор = положение.исполнитьЗапрос(ясз)) {
      МетаДанныеОтветногоНабора мета = набор.метаДанные();
      int колКолонок = мета.количествоКолонок();
      while (набор.следующий()) {
        Map<String, Object> ряд = new LinkedHashMap<>();
        for (int i = 1; i <= колКолонок; i++) {
          String colName = мета.имяКолонки(i);
          ряд.put(colName, набор.достатьОбъект(i));
        }
        ряды.add(ряд);
      }
    }
    return ряды;
  }

  private void подготовитьТаблицуМариныКВводу(String таблица, String колонки, List<Map<String, Object>> ряды) throws ИсключениеЯСЗ, JSONException {
    СтрочныйСтроитель значения = new СтрочныйСтроитель();
    for (Map<String, Object> ряд : ряды) {
      значения.прикрепить("(");
      for (Map.Entry<String, Object> entry : ряд.entrySet()) {
        var значение = entry.getValue();
        switch (значение) {
          case String строка -> {
            String значениеВСтепени64 = Строки.закодироватьВСтепень64(строка);
            значения.прикрепить(", FROM_BASE64('").прикрепить(значениеВСтепени64).прикрепить("')");
          }
          case Number число -> значения.прикрепить(", ").прикрепить(число);
          case byte[] байты -> {
            String значениеВСтепени64 = Строки.закодироватьВСтепень64(байты);
            значения.прикрепить(", FROM_BASE64('").прикрепить(значениеВСтепени64).прикрепить("')");
          }
          case null -> значения.прикрепить(", null"); // Handle null values explicitly
          default -> throw new ИсключениеНелегальногоАргумента("Неизвестный тип данных: " + значение.getClass());
        }
      }
      значения.прикрепить("),");
    }

    var отформатированныеЗначения = Строки.заменить(значения.получить(), "(, ", "(");
    отформатированныеЗначения = Строки.заменитьРегулярнымВырожением(отформатированныеЗначения, "\\),$", ")");
    вставитьДанныеВМарию(таблица, колонки, отформатированныеЗначения);
  }

  private void updateRowidFor(String table) throws ИсключениеЯСЗ {
    Система.печатьстр("Обновляю идентификаторы на значение rowid...");
    try (Положение положение = подключениеМарии().создатьПоложение()) {
      положение.исполнить("UPDATE " + table + " SET id = rowid WHERE id IS NULL;");
    }
  }

  private void пуск() throws SQLException, JSONException {
    List<String> tablesWithNullId = Arrays.asList("co_art_map", "co_blockdata_map", "co_entity_map", "co_material_map", "co_world");

    try (Положение положение = подключениеМарии().создатьПоложение()) {
      положение.исполнить("ALTER DATABASE " + окружение.getProperty("MYSQL_DATABASE") + " CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;");
      положение.исполнить("SET FOREIGN_KEY_CHECKS=0;");
      положение.исполнить("SET UNIQUE_CHECKS=0;");
      положение.исполнить("SET autocommit=0;");

      List<String> tables = таблицыЛайта();
      for (String table : tables) {
        очиститьТаблицуМарии(table);
        String columns = колонкиТаблицыЛайта(table);
        int totalRows = количествоСтрокЛайта(table);

        Система.печатьф("Migrating %s with %d records...\n", table, totalRows);

        int i = 0;
        while (i < totalRows) {
          List<Map<String, Object>> rows = заказатьЛайт(table, i);
          подготовитьТаблицуМариныКВводу(table, columns, rows);

          i = Math.min(i + Integer.parseInt(окружение.getProperty("OFFSET")), totalRows);
          long dt = System.currentTimeMillis() - времяНачала;
          long hours = (dt / 1000) / 3600;
          long minutes = ((dt / 1000) % 3600) / 60;
          long seconds = (dt / 1000) % 60;

          Система.печатьф("\33[2K\r%d/%d (%.2f%%)... [%02d:%02d:%02d] ", i, totalRows, (float) i / totalRows * 100, hours, minutes, seconds);
        }

        if (tablesWithNullId.contains(table)) {
          updateRowidFor(table);
        }

        Система.печатьстр("готово\n");
      }

      Система.ошибкастр("Error count: " + ошибок);
      if (ошибок > 0) {
        Система.ошибкастр("Error tables: " + таблицыСОшибками);
      }

      положение.исполнить("SET FOREIGN_KEY_CHECKS=1;");
      положение.исполнить("SET UNIQUE_CHECKS=1;");
      положение.исполнить("SET autocommit=1;");
    }
    if (подклМарии != null) {
      подклМарии.закрыть();
    }
    if (подклЛайта != null) {
      подклЛайта.закрыть();
    }
  }
}