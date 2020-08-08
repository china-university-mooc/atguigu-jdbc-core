package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateStatement implements SqlStatement {

  private static final String TEMPLATE = "update `%s` set %s where %s";

  private final TableMeta table;
  private final Object entity;

  public UpdateStatement(TableMeta table, Object entity) {
    this.table = table;
    this.entity = entity;
  }

  @Override
  public String getSql() {
    String setClause = table.getPlainColumns().stream()
        .map(c -> c.getName() + " = ?")
        .collect(Collectors.joining(", "));
    String whereClause = table.getIdColumn().getName() + " = ?";

    return String.format(TEMPLATE, table.getName(), setClause, whereClause);
  }

  @Override
  public Object[] getParams() {
    List<Object> values = table.getPlainValues(entity);
    values.add(table.getIdValue(entity));
    return values.toArray();
  }
}
