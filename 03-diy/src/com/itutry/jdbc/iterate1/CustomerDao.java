package com.itutry.jdbc.iterate1;

import java.sql.Connection;
import java.sql.Date;

public interface CustomerDao {

  Date getMaxBirth(Connection conn);
}
