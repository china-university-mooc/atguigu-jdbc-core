package com.itutry.jdbc.iterate1.util;

import com.itutry.jdbc.iterate1.annotaion.Column;
import java.lang.reflect.Field;
import java.util.Optional;

public class ColumnMeta {

  private String name;
  private String label;
  private Field field;

  public ColumnMeta(Field field) {
    this.field = field;
    this.name = parseColumnName(field);
    this.label = parseColumnLabel(field);
  }

  private String parseColumnName(Field field) {
    Column column = field.getAnnotation(Column.class);
    return Optional.ofNullable(column)
        .map(Column::value)
        .orElse(field.getName());
  }

  public String parseColumnLabel(Field field) {
    return field.getName();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Object getValue(Object obj) {
    Object value = null;
    try {
      field.setAccessible(true);
      value = field.get(obj);
    } catch (IllegalAccessException e) {
      JdbcUtils.quietlyHandleException(e);
    }
    return value;
  }
}
