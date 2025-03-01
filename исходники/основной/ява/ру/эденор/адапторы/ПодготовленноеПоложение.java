package ру.эденор.адапторы;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("NonAsciiCharacters")
public class ПодготовленноеПоложение implements AutoCloseable {
  private final PreparedStatement положение;

  public ПодготовленноеПоложение(PreparedStatement положение) {
    this.положение = положение;
  }

  public МетаДанныеОтветногоНабора метаДанные() throws ИсключениеЯСЗ {
    try {
      return new МетаДанныеОтветногоНабора(положение.getMetaData());
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  @Override
  public void close() throws ИсключениеЯСЗ {
    try {
      положение.close();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
