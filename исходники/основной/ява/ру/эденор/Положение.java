package ру.эденор;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("NonAsciiCharacters")
public class Положение implements AutoCloseable{
  private final Statement положение;

  public Положение(Statement положение) {
    this.положение = положение;
  }

  @Override
  public void close() throws ИсключениеЯСЗ {
    try {
      положение.close();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public void исполнить(String запрос) throws ИсключениеЯСЗ {
    try {
      положение.execute(запрос);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public ОтветныйНабор исполнитьЗапрос(String запрос) throws ИсключениеЯСЗ {
    try {
      return new ОтветныйНабор(положение.executeQuery(запрос));
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
