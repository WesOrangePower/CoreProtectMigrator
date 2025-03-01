package ру.эденор;

@SuppressWarnings("NonAsciiCharacters")
public class Класс {
  public static Class<?> поИмени(String имя) throws ИсключениеКлассНеНайден {
    try {
      return Class.forName(имя);
    } catch (ClassNotFoundException e) {
      throw new ИсключениеКлассНеНайден(имя, e);
    }
  }
}
