package ру.эденор.адапторы;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class СписокНаОсновеМассива<Тип> implements Список<Тип> {
  private ArrayList<Тип> внутреннийСписок = new ArrayList<>();

  public static <Тип> СписокНаОсновеМассива<Тип> изЯвавогоСписка(List<Тип> явавыйСписок) {
    var список = new СписокНаОсновеМассива<Тип>();
    список.внутреннийСписок = new ArrayList<>(явавыйСписок);
    return список;
  }

  public void добавить(Тип элемент) {
    внутреннийСписок.add(элемент);
  }

  @Override
  public Iterator<Тип> iterator() {
    return внутреннийСписок.iterator();
  }

  @Override
  public void forEach(Consumer<? super Тип> действие) {
    внутреннийСписок.forEach(действие);
  }

  @Override
  public Spliterator<Тип> spliterator() {
    return внутреннийСписок.spliterator();
  }

  public boolean содержит(Тип кандидат) {
    return внутреннийСписок.contains(кандидат);
  }
}
