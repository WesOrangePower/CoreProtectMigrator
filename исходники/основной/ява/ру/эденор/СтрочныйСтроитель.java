package ру.эденор;

@SuppressWarnings("NonAsciiCharacters")
public class СтрочныйСтроитель {
  private final StringBuilder builder = new StringBuilder();

  public СтрочныйСтроитель() {
  }

  public СтрочныйСтроитель(String начальноеЗначение) {
    builder.append(начальноеЗначение);
  }

  public СтрочныйСтроитель прикрепить(String строка) {
    builder.append(строка);
    return this;
  }

  public СтрочныйСтроитель прикрепить(int число) {
    builder.append(число);
    return this;
  }

  public String получить() {
    return builder.toString();
  }

  public void прикрепить(Number числовоеЗначение) {
    builder.append(числовоеЗначение);
  }
}
