package ру.эденор.адапторы;

import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("NonAsciiCharacters")
public class УправляющийДрайверами {
  public static Подключение подключиться(String ссылка, String пользователь, String пароль)
      throws ИсключениеЯСЗ {
    try {
      return new Подключение(DriverManager.getConnection(ссылка, пользователь, пароль));
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public static Подключение подключиться(String ссылка) throws ИсключениеЯСЗ {
    try {
      return new Подключение(DriverManager.getConnection(ссылка));
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
