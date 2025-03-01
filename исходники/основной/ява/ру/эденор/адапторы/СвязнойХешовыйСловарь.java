package ру.эденор.адапторы;

import java.util.LinkedHashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class СвязнойХешовыйСловарь<ТипКлюча, ТипЗначения>
    implements Словарь<ТипКлюча, ТипЗначения> {
  private final LinkedHashMap<ТипКлюча, ТипЗначения> внутреннийСловарь = new LinkedHashMap<>();

  @Override
  public void записать(ТипКлюча ключ, ТипЗначения значение) {
    внутреннийСловарь.put(ключ, значение);
  }

  @Override
  public Set<Запись<ТипКлюча, ТипЗначения>> наборЗаписей() {
    return внутреннийСловарь.entrySet().stream().map(Запись::изЯвы).collect(Collectors.toSet());
  }
}
