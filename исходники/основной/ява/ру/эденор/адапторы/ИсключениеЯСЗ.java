package ру.эденор.адапторы;

import java.sql.SQLException;

@SuppressWarnings("NonAsciiCharacters")
public class ИсключениеЯСЗ extends SQLException {
  public ИсключениеЯСЗ(SQLException искл) {
    this(искл.getMessage(), искл.getSQLState(), искл.getErrorCode(), искл.getCause());
  }

  public ИсключениеЯСЗ(String reason, String sqlState, int vendorCode, Throwable cause) {
    super(reason, sqlState, vendorCode, cause);
  }

  public int кодОшибки() {
    return getErrorCode();
  }

  public String сообщение() {
    return getMessage();
  }
}
