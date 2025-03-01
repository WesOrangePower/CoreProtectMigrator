package ру.эденор;

import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("NonAsciiCharacters")
public class Подключение {
  Connection соединение;
  public Подключение(Connection соединение) {
    this.соединение = соединение;
  }

  public Положение создатьПоложение() throws ИсключениеЯСЗ {
    try {
      return new Положение(соединение.createStatement());
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public void включитьАвтоПрименение() throws ИсключениеЯСЗ {
    установитьЗначениеАвтоматическогоПрименения(true);
  }
  public void выключитьАвтоПрименение() throws ИсключениеЯСЗ {
    установитьЗначениеАвтоматическогоПрименения(false);
  }

  public void установитьЗначениеАвтоматическогоПрименения(boolean b) throws ИсключениеЯСЗ {
    try {
      соединение.setAutoCommit(b);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public void применить() throws ИсключениеЯСЗ {
    try {
      соединение.commit();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public void закрыть() throws ИсключениеЯСЗ {
    try {
      соединение.close();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public ПодготовленноеПоложение подготовленныйЗапрос(String запрос) throws ИсключениеЯСЗ {
    try {
      return new ПодготовленноеПоложение(соединение.prepareStatement(запрос));
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
