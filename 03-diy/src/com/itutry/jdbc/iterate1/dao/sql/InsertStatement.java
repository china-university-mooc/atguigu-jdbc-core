package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.ColumnMeta;
import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import java.util.stream.Collectors;

public class InsertStatement implements SqlStatement {

  private static final String TEMPLATE = "insert into `%s`(%s) values (%s)";

  private final TableMeta table;
  private final Object entity;

  public InsertStatement(TableMeta table, Object entity) {
    this.table = table;
    this.entity = entity;
  }

  @Override
  public String getSql() {
    String insertClause = table.getColumns().stream()
        .map(ColumnMeta::getName)
        .collect(Collectors.joining(", "));

    String placeholderStr = table.getColumns().stream()
        .map(field -> "?")
        .collect(Collectors.joining(","));

    return String.format(TEMPLATE, table.getName(), insertClause, placeholderStr);
  }

  @Override
  public Object[] getParams() {
    return table.getValues(entity).toArray();
  }
}
