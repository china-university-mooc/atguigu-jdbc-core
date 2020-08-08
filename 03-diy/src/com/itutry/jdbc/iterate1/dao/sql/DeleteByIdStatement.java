package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;

public class DeleteByIdStatement implements SqlStatement {

  private static final String TEMPLATE = "delete from `%s` where %s";

  private final TableMeta table;
  private final Object id;

  public DeleteByIdStatement(TableMeta table, Object id) {
    this.table = table;
    this.id = id;
  }

  @Override
  public String getSql() {
    String whereClause = table.getIdColumn().getName() + " = ?";
    return String.format(TEMPLATE, table.getName(), whereClause);
  }

  @Override
  public Object[] getParams() {
    return new Object[]{id};
  }
}
