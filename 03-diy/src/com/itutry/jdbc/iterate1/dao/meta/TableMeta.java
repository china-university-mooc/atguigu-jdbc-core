package com.itutry.jdbc.iterate1.dao.meta;

import com.itutry.jdbc.iterate1.annotaion.Id;
import com.itutry.jdbc.iterate1.annotaion.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableMeta {

  private static final String ID_NAME = "id";

  private String name;
  private ColumnMeta idColumn;
  private List<ColumnMeta> plainColumns;
  private List<ColumnMeta> columns = new ArrayList<>();

  public TableMeta(Class<?> type) {
    this.name = parseTableName(type);
    this.idColumn = parseIdColumn(type);
    this.plainColumns = parsePlainColumns(type);
    this.columns.add(idColumn);
    this.columns.addAll(plainColumns);
  }

  private List<ColumnMeta> parsePlainColumns(Class<?> type) {
    return Arrays.stream(type.getDeclaredFields())
        .filter(field -> !isIdField(field))
        .map(ColumnMeta::new)
        .collect(Collectors.toList());
  }

  private ColumnMeta parseIdColumn(Class<?> type) {
    List<Field> fields = Arrays.stream(type.getDeclaredFields())
        .filter(this::isIdField)
        .collect(Collectors.toList());
    if (fields.size() == 1) {
      return new ColumnMeta(fields.get(0));
    }

    throw new RuntimeException("表中ID列的个数不为一");
  }

  private String parseTableName(Class<?> type) {
    Table table = type.getAnnotation(Table.class);
    return Optional.ofNullable(table)
        .map(Table::value)
        .orElse(type.getSimpleName());
  }

  private boolean isIdField(Field field) {
    Id id = field.getAnnotation(Id.class);
    return id != null || ID_NAME.equalsIgnoreCase(field.getName());
  }

  public String getName() {
    return name;
  }

  public ColumnMeta getIdColumn() {
    return idColumn;
  }

  public List<ColumnMeta> getPlainColumns() {
    return plainColumns;
  }

  public List<ColumnMeta> getColumns() {
    return columns;
  }

  public Object getIdValue(Object obj) {
    return idColumn.getValue(obj);
  }

  public List<Object> getPlainValues(Object obj) {
    return plainColumns.stream()
        .map(c -> c.getValue(obj))
        .collect(Collectors.toList());
  }

  public List<Object> getValues(Object obj) {
    return columns.stream()
        .map(c -> c.getValue(obj))
        .collect(Collectors.toList());
  }
}
