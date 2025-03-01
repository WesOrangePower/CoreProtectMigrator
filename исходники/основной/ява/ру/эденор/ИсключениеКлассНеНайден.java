package ру.эденор;

@SuppressWarnings("NonAsciiCharacters")
public class ИсключениеКлассНеНайден extends ClassNotFoundException {
  public ИсключениеКлассНеНайден(String текст, Throwable причина) {
    super(текст, причина);
  }
}
