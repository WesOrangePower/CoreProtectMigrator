package ру.эденор.адапторы;

public class Объекты {
  public static boolean естьНеПустые(Object... объекты) {
    for (Object объект : объекты) if (объект != null) return true;
    return false;
  }
  public static boolean всеНеПустые(Object... объекты) {
    for (Object объект : объекты) if (объект == null) return false;
    return true;
  }

  public static <Тип> Тип значениеИли(Тип экземпляр, Тип значениеПоУмолчанию) {
    if (экземпляр != null) return экземпляр;
    return значениеПоУмолчанию;
  }
}
