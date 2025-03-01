package ру.эденор.адапторы;

@SuppressWarnings("NonAsciiCharacters")
public class ИсключениеКлассНеНайден extends ClassNotFoundException {
  public ИсключениеКлассНеНайден(String текст, Throwable причина) {
    super(текст, причина);
  }
}
