package com.itutry.jdbc.iterate1.dao;

import java.sql.Connection;
import java.util.List;

public interface Dao<T> {

  int insert(Connection conn, T obj);

  int deleteById(Connection conn, Object id);

  int update(Connection conn, T obj);

  T getById(Connection conn, Object id);

  List<T> getAll(Connection conn);

  Long count(Connection conn);
}
