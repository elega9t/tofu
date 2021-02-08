package com.elega9t.tofu.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {
    
    public static List<List<Object>> read(ResultSet resultSet, String... columns) throws SQLException {
        List<List<Object>> rows = new ArrayList<>();
        while(resultSet.next()) {
            List row = new ArrayList<>();
            for (String column: columns) {
                row.add(resultSet.getObject(column));
            }
            rows.add(row);
        }
        return rows;
    }
    
}
