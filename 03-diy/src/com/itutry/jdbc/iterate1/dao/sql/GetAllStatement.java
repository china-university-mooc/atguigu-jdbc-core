package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;
import java.util.stream.Collectors;

public class GetAllStatement implements SqlStatement {

  private static final String TEMPLATE = "select %s from `%s`";

  private final TableMeta table;

  public GetAllStatement(TableMeta table) {
    this.table = table;
  }

  @Override
  public String getSql() {
    String selectClause = table.getColumns().stream()
        .map(c -> c.getName() + " " + c.getLabel())
        .collect(Collectors.joining(", "));
    return String.format(TEMPLATE, selectClause, table.getName());
  }

  @Override
  public Object[] getParams() {
    return new Object[0];
  }
}
