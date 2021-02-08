package com.elega9t.tofu.persistance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elega9t
 */
public class StringJDBCTransform implements JDBCTransform<String> {

    private final String name;

    public StringJDBCTransform(String name) {
        this.name = name;
    }

    @Override
    public List<String> fromResultSet(ResultSet resultSet) throws SQLException {
            List<String> result = new ArrayList<>();
            while(resultSet.next()) {
                String v = resultSet.getString(this.name);
                result.add(v);
            }
            return result;
    }

    @Override
    public PreparedStatement insertStatement(String t, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PreparedStatement updateStatement(String t, Connection connection) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
