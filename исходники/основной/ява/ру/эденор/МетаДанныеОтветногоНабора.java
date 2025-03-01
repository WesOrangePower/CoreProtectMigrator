package ру.эденор;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class МетаДанныеОтветногоНабора {
  private final ResultSetMetaData metaData;

  public МетаДанныеОтветногоНабора(ResultSetMetaData metaData) {
    this.metaData = metaData;
  }

  public int количествоКолонок() throws ИсключениеЯСЗ {
    try {
      return metaData.getColumnCount();
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }

  public String имяКолонки(int i) throws ИсключениеЯСЗ {
    try {
      return metaData.getColumnName(i);
    } catch (SQLException e) {
      throw new ИсключениеЯСЗ(e);
    }
  }
}
