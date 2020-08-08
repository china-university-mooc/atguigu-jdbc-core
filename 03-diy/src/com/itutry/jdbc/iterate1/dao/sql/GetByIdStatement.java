package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import java.util.stream.Collectors;

public class GetByIdStatement implements SqlStatement {

  private static final String TEMPLATE = "select %s from `%s` where %s";

  private final TableMeta table;
  private final Object id;

  public GetByIdStatement(TableMeta table, Object id) {
    this.table = table;
    this.id = id;
  }

  @Override
  public String getSql() {
    String selectClause = table.getColumns().stream()
        .map(c -> c.getName() + " " + c.getLabel())
        .collect(Collectors.joining(", "));
    String whereClause = table.getIdColumn().getName() + " = ?";
    return String
        .format(TEMPLATE, selectClause, table.getName(), whereClause);
  }

  @Override
  public Object[] getParams() {
    return new Object[]{id};
  }
}
