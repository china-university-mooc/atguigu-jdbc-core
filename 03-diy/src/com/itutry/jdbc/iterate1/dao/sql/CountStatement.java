package com.itutry.jdbc.iterate1.dao.sql;

import com.itutry.jdbc.iterate1.dao.meta.TableMeta;

public class CountStatement implements SqlStatement {

  private static final String TEMPLATE = "select count(*) from `%s`";

  private final TableMeta table;

  public CountStatement(TableMeta table) {
    this.table = table;
  }

  @Override
  public String getSql() {
    return String.format(TEMPLATE, table.getName());
  }

  @Override
  public Object[] getParams() {
    return new Object[0];
  }
}
