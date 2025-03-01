package ру.эденор.адапторы;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("NonAsciiCharacters")
public class ОтветныйНабор implements AutoCloseable {
  private final ResultSet ответныйНабор;

  public ОтветныйНабор(ResultSet ответныйНабор) {
    this.ответныйНабор = ответныйНабор;
  }

  @Override
  public void close() throws ИсключениеЯСЗ {
    try {
      ответныйНабор.close();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public boolean следующий() throws ИсключениеЯСЗ {
    try {
      return ответныйНабор.next();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public String достатьСтроку(String имя) throws ИсключениеЯСЗ {
    try {
      return ответныйНабор.getString(имя);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public int достатьЦел(int индекс) throws ИсключениеЯСЗ {
    try {
      return ответныйНабор.getInt(индекс);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public Object достатьОбъект(int индекс) throws ИсключениеЯСЗ {
    try {
      return ответныйНабор.getObject(индекс);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public МетаДанныеОтветногоНабора метаДанные() throws ИсключениеЯСЗ {
    try {
      return new МетаДанныеОтветногоНабора(ответныйНабор.getMetaData());
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
